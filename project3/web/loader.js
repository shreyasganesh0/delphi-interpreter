// Minimal WebAssembly loader for a Pascal program compiled with this project.
//
// Usage:
//   1. Compile your Pascal program to LLVM IR:    ./compile.sh tests/test7.pas
//   2. Compile the .ll to a wasm module (one of):
//        a) emscripten:   emcc tests/test7.ll -o web/output.html -s EXPORTED_FUNCTIONS='["_main"]'
//                         (emcc emits its own loader; you can use that directly)
//        b) raw LLVM:     llc -march=wasm32 tests/test7.ll -o tests/test7.s
//                         wasm-ld --no-entry --export=main tests/test7.o -o web/output.wasm
//                         (you must provide JS shims for printf/scanf — see README)
//   3. Serve this directory over HTTP (file:// won't allow fetch):
//        python3 -m http.server --directory web 8000
//   4. Open http://localhost:8000/ in a browser and click Run.

const out = document.getElementById("out");
function log(s) { out.textContent += s; }

async function run() {
  out.textContent = "";

  // Minimal printf shim: read a NUL-terminated C string from wasm linear memory at ptr.
  let memory;
  function readCStr(ptr) {
    const bytes = new Uint8Array(memory.buffer);
    let end = ptr;
    while (bytes[end] !== 0) end++;
    return new TextDecoder("utf-8").decode(bytes.subarray(ptr, end));
  }

  // The shape of the printf import depends on how the wasm was produced. The shim below
  // supports the simplest case (variadic printf with a format-string pointer + zero or more
  // i32 / double / pointer args). For Emscripten output, prefer using emcc's generated loader
  // instead — it provides a complete libc.
  const imports = {
    env: {
      printf: function(fmtPtr) {
        const fmt = readCStr(fmtPtr);
        // simplistic %s/%d/%g substitution; a real impl would parse varargs
        log(fmt.replace(/%[sdg]/g, "?"));
        return 0;
      },
      scanf:  function() { return 0; },
      puts:   function(p) { log(readCStr(p) + "\n"); return 0; },
    },
  };

  try {
    const resp = await fetch("output.wasm");
    const bytes = await resp.arrayBuffer();
    const { instance } = await WebAssembly.instantiate(bytes, imports);
    memory = instance.exports.memory;
    const result = instance.exports.main();
    log(`\n[program returned ${result}]\n`);
  } catch (err) {
    log("Error: " + err.message + "\n");
  }
}

document.getElementById("run").addEventListener("click", run);
