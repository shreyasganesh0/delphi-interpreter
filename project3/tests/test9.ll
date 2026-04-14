; ModuleID = 'pascal'
source_filename = "pascal"

declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)
declare i32 @puts(i8*)

@.str.0 = private unnamed_addr constant [4 x i8] c">> \00"
@.str.1 = private unnamed_addr constant [6 x i8] c"%s%s\0A\00"
@.str.2 = private unnamed_addr constant [18 x i8] c"=== Factorial ===\00"
@.str.3 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.4 = private unnamed_addr constant [11 x i8] c"Factorial(\00"
@.str.5 = private unnamed_addr constant [5 x i8] c") = \00"
@.str.6 = private unnamed_addr constant [10 x i8] c"%s%d%s%d\0A\00"
@.str.7 = private unnamed_addr constant [2 x i8] c"\0A\00"
@.str.8 = private unnamed_addr constant [18 x i8] c"=== Fibonacci ===\00"
@.str.9 = private unnamed_addr constant [2 x i8] c" \00"
@.str.10 = private unnamed_addr constant [5 x i8] c"%d%s\00"
@.str.11 = private unnamed_addr constant [12 x i8] c"=== Max ===\00"
@.str.12 = private unnamed_addr constant [13 x i8] c"Max(3, 7) = \00"
@.str.13 = private unnamed_addr constant [6 x i8] c"%s%d\0A\00"
@.str.14 = private unnamed_addr constant [14 x i8] c"Max(10, 2) = \00"
@.str.15 = private unnamed_addr constant [24 x i8] c"Hello from a procedure!\00"

@g_n = global i32 0

define i32 @p_factorial(i32 %arg0) {
entry:
  %n.addr = alloca i32
  %result.addr = alloca i32
  store i32 %arg0, i32* %n.addr
  %0 = load i32, i32* %n.addr
  %1 = icmp sle i32 %0, 1
  br i1 %1, label %if.then.0, label %if.else.2
if.then.0:
  store i32 1, i32* %result.addr
  br label %if.end.1
if.else.2:
  %2 = load i32, i32* %n.addr
  %3 = load i32, i32* %n.addr
  %4 = sub i32 %3, 1
  %5 = call i32 @p_factorial(i32 %4)
  %6 = mul i32 %2, %5
  store i32 %6, i32* %result.addr
  br label %if.end.1
if.end.1:
  %7 = load i32, i32* %result.addr
  ret i32 %7
}

define i32 @p_fibonacci(i32 %arg0) {
entry:
  %n.addr = alloca i32
  %result.addr = alloca i32
  store i32 %arg0, i32* %n.addr
  %0 = load i32, i32* %n.addr
  %1 = icmp sle i32 %0, 0
  br i1 %1, label %if.then.0, label %if.else.2
if.then.0:
  store i32 0, i32* %result.addr
  br label %if.end.1
if.else.2:
  %2 = load i32, i32* %n.addr
  %3 = icmp eq i32 %2, 1
  br i1 %3, label %if.then.3, label %if.else.5
if.then.3:
  store i32 1, i32* %result.addr
  br label %if.end.4
if.else.5:
  %4 = load i32, i32* %n.addr
  %5 = sub i32 %4, 1
  %6 = call i32 @p_fibonacci(i32 %5)
  %7 = load i32, i32* %n.addr
  %8 = sub i32 %7, 2
  %9 = call i32 @p_fibonacci(i32 %8)
  %10 = add i32 %6, %9
  store i32 %10, i32* %result.addr
  br label %if.end.4
if.end.4:
  br label %if.end.1
if.end.1:
  %11 = load i32, i32* %result.addr
  ret i32 %11
}

define i32 @p_max(i32 %arg0, i32 %arg1) {
entry:
  %a.addr = alloca i32
  %b.addr = alloca i32
  %result.addr = alloca i32
  store i32 %arg0, i32* %a.addr
  store i32 %arg1, i32* %b.addr
  %0 = load i32, i32* %a.addr
  %1 = load i32, i32* %b.addr
  %2 = icmp sgt i32 %0, %1
  br i1 %2, label %if.then.0, label %if.else.2
if.then.0:
  %3 = load i32, i32* %a.addr
  store i32 %3, i32* %result.addr
  br label %if.end.1
if.else.2:
  %4 = load i32, i32* %b.addr
  store i32 %4, i32* %result.addr
  br label %if.end.1
if.end.1:
  %5 = load i32, i32* %result.addr
  ret i32 %5
}

define void @p_printline(i8* %arg0) {
entry:
  %msg.addr = alloca i8*
  store i8* %arg0, i8** %msg.addr
  %0 = load i8*, i8** %msg.addr
  %1 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.0, i32 0, i32 0), i8* %0)
  ret void
}

define i32 @main() {
entry:
  %for.end.0.ptr = alloca i32
  %for.end.6.ptr = alloca i32
  %0 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([18 x i8], [18 x i8]* @.str.2, i32 0, i32 0))
  store i32 7, i32* %for.end.0.ptr
  store i32 0, i32* @g_n
  br label %for.entry.1
for.entry.1:
  %1 = load i32, i32* @g_n
  %2 = load i32, i32* %for.end.0.ptr
  %3 = icmp sle i32 %1, %2
  br i1 %3, label %for.body.2, label %for.end.5
for.body.2:
  %4 = load i32, i32* @g_n
  %5 = load i32, i32* @g_n
  %6 = call i32 @p_factorial(i32 %5)
  %7 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([10 x i8], [10 x i8]* @.str.6, i32 0, i32 0), i8* getelementptr inbounds ([11 x i8], [11 x i8]* @.str.4, i32 0, i32 0), i32 %4, i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.str.5, i32 0, i32 0), i32 %6)
  br label %for.post.3
for.post.3:
  %8 = load i32, i32* @g_n
  %9 = load i32, i32* %for.end.0.ptr
  %10 = icmp eq i32 %8, %9
  br i1 %10, label %for.end.5, label %for.step.4
for.step.4:
  %11 = add i32 %8, 1
  store i32 %11, i32* @g_n
  br label %for.body.2
for.end.5:
  %12 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.7, i32 0, i32 0))
  %13 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([18 x i8], [18 x i8]* @.str.8, i32 0, i32 0))
  store i32 10, i32* %for.end.6.ptr
  store i32 0, i32* @g_n
  br label %for.entry.7
for.entry.7:
  %14 = load i32, i32* @g_n
  %15 = load i32, i32* %for.end.6.ptr
  %16 = icmp sle i32 %14, %15
  br i1 %16, label %for.body.8, label %for.end.11
for.body.8:
  %17 = load i32, i32* @g_n
  %18 = call i32 @p_fibonacci(i32 %17)
  %19 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.str.10, i32 0, i32 0), i32 %18, i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.9, i32 0, i32 0))
  br label %for.post.9
for.post.9:
  %20 = load i32, i32* @g_n
  %21 = load i32, i32* %for.end.6.ptr
  %22 = icmp eq i32 %20, %21
  br i1 %22, label %for.end.11, label %for.step.10
for.step.10:
  %23 = add i32 %20, 1
  store i32 %23, i32* @g_n
  br label %for.body.8
for.end.11:
  %24 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.7, i32 0, i32 0))
  %25 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.7, i32 0, i32 0))
  %26 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([12 x i8], [12 x i8]* @.str.11, i32 0, i32 0))
  %27 = call i32 @p_max(i32 3, i32 7)
  %28 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.13, i32 0, i32 0), i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str.12, i32 0, i32 0), i32 %27)
  %29 = call i32 @p_max(i32 10, i32 2)
  %30 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.13, i32 0, i32 0), i8* getelementptr inbounds ([14 x i8], [14 x i8]* @.str.14, i32 0, i32 0), i32 %29)
  %31 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.7, i32 0, i32 0))
  call void @p_printline(i8* getelementptr inbounds ([24 x i8], [24 x i8]* @.str.15, i32 0, i32 0))
  ret i32 0
}

