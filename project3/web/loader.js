const out = document.getElementById("out");
function log(s) { out.textContent += s; }

async function run() {
  out.textContent = "";

  let memory;
  function readCStr(ptr) {
    const bytes = new Uint8Array(memory.buffer);
    let end = ptr;
    while (bytes[end] !== 0) end++;
    return new TextDecoder("utf-8").decode(bytes.subarray(ptr, end));
  }

  const imports = {
    env: {
      printf: function(fmtPtr) {
        const fmt = readCStr(fmtPtr);
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
