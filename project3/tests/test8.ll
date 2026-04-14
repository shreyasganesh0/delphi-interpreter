; ModuleID = 'pascal'
source_filename = "pascal"

declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)
declare i32 @puts(i8*)

@.str.0 = private unnamed_addr constant [23 x i8] c"=== For with Break ===\00"
@.str.1 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.2 = private unnamed_addr constant [25 x i8] c"First i where i*i > 50: \00"
@.str.3 = private unnamed_addr constant [6 x i8] c"%s%d\0A\00"
@.str.4 = private unnamed_addr constant [26 x i8] c"=== For with Continue ===\00"
@.str.5 = private unnamed_addr constant [2 x i8] c" \00"
@.str.6 = private unnamed_addr constant [5 x i8] c"%d%s\00"
@.str.7 = private unnamed_addr constant [2 x i8] c"\0A\00"
@.str.8 = private unnamed_addr constant [32 x i8] c"=== Nested Loops with Break ===\00"
@.str.9 = private unnamed_addr constant [26 x i8] c"=== Repeat with Break ===\00"
@.str.10 = private unnamed_addr constant [16 x i8] c"Stopped at i = \00"

@g_i = global i32 0
@g_j = global i32 0
@g_found = global i32 0

define i32 @main() {
entry:
  %for.end.0.ptr = alloca i32
  %for.end.8.ptr = alloca i32
  %for.end.16.ptr = alloca i32
  %for.end.22.ptr = alloca i32
  %0 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([23 x i8], [23 x i8]* @.str.0, i32 0, i32 0))
  store i32 0, i32* @g_found
  store i32 20, i32* %for.end.0.ptr
  store i32 1, i32* @g_i
  br label %for.entry.1
for.entry.1:
  %1 = load i32, i32* @g_i
  %2 = load i32, i32* %for.end.0.ptr
  %3 = icmp sle i32 %1, %2
  br i1 %3, label %for.body.2, label %for.end.5
for.body.2:
  %4 = load i32, i32* @g_i
  %5 = load i32, i32* @g_i
  %6 = mul i32 %4, %5
  %7 = icmp sgt i32 %6, 50
  br i1 %7, label %if.then.6, label %if.end.7
if.then.6:
  %8 = load i32, i32* @g_i
  store i32 %8, i32* @g_found
  br label %for.end.5
if.end.7:
  br label %for.post.3
for.post.3:
  %9 = load i32, i32* @g_i
  %10 = load i32, i32* %for.end.0.ptr
  %11 = icmp eq i32 %9, %10
  br i1 %11, label %for.end.5, label %for.step.4
for.step.4:
  %12 = add i32 %9, 1
  store i32 %12, i32* @g_i
  br label %for.body.2
for.end.5:
  %13 = load i32, i32* @g_found
  %14 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([25 x i8], [25 x i8]* @.str.2, i32 0, i32 0), i32 %13)
  %15 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([26 x i8], [26 x i8]* @.str.4, i32 0, i32 0))
  store i32 10, i32* %for.end.8.ptr
  store i32 1, i32* @g_i
  br label %for.entry.9
for.entry.9:
  %16 = load i32, i32* @g_i
  %17 = load i32, i32* %for.end.8.ptr
  %18 = icmp sle i32 %16, %17
  br i1 %18, label %for.body.10, label %for.end.13
for.body.10:
  %19 = load i32, i32* @g_i
  %20 = srem i32 %19, 3
  %21 = icmp eq i32 %20, 0
  br i1 %21, label %if.then.14, label %if.end.15
if.then.14:
  br label %for.post.11
if.end.15:
  %22 = load i32, i32* @g_i
  %23 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.str.6, i32 0, i32 0), i32 %22, i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.5, i32 0, i32 0))
  br label %for.post.11
for.post.11:
  %24 = load i32, i32* @g_i
  %25 = load i32, i32* %for.end.8.ptr
  %26 = icmp eq i32 %24, %25
  br i1 %26, label %for.end.13, label %for.step.12
for.step.12:
  %27 = add i32 %24, 1
  store i32 %27, i32* @g_i
  br label %for.body.10
for.end.13:
  %28 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.7, i32 0, i32 0))
  %29 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([32 x i8], [32 x i8]* @.str.8, i32 0, i32 0))
  store i32 5, i32* %for.end.16.ptr
  store i32 1, i32* @g_i
  br label %for.entry.17
for.entry.17:
  %30 = load i32, i32* @g_i
  %31 = load i32, i32* %for.end.16.ptr
  %32 = icmp sle i32 %30, %31
  br i1 %32, label %for.body.18, label %for.end.21
for.body.18:
  store i32 5, i32* %for.end.22.ptr
  store i32 1, i32* @g_j
  br label %for.entry.23
for.entry.23:
  %33 = load i32, i32* @g_j
  %34 = load i32, i32* %for.end.22.ptr
  %35 = icmp sle i32 %33, %34
  br i1 %35, label %for.body.24, label %for.end.27
for.body.24:
  %36 = load i32, i32* @g_j
  %37 = load i32, i32* @g_i
  %38 = icmp sgt i32 %36, %37
  br i1 %38, label %if.then.28, label %if.end.29
if.then.28:
  br label %for.end.27
if.end.29:
  %39 = load i32, i32* @g_j
  %40 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.str.6, i32 0, i32 0), i32 %39, i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.5, i32 0, i32 0))
  br label %for.post.25
for.post.25:
  %41 = load i32, i32* @g_j
  %42 = load i32, i32* %for.end.22.ptr
  %43 = icmp eq i32 %41, %42
  br i1 %43, label %for.end.27, label %for.step.26
for.step.26:
  %44 = add i32 %41, 1
  store i32 %44, i32* @g_j
  br label %for.body.24
for.end.27:
  %45 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.7, i32 0, i32 0))
  br label %for.post.19
for.post.19:
  %46 = load i32, i32* @g_i
  %47 = load i32, i32* %for.end.16.ptr
  %48 = icmp eq i32 %46, %47
  br i1 %48, label %for.end.21, label %for.step.20
for.step.20:
  %49 = add i32 %46, 1
  store i32 %49, i32* @g_i
  br label %for.body.18
for.end.21:
  %50 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([26 x i8], [26 x i8]* @.str.9, i32 0, i32 0))
  store i32 0, i32* @g_i
  br label %repeat.body.30
repeat.body.30:
  %51 = load i32, i32* @g_i
  %52 = add i32 %51, 1
  store i32 %52, i32* @g_i
  %53 = load i32, i32* @g_i
  %54 = icmp eq i32 %53, 3
  br i1 %54, label %if.then.33, label %if.end.34
if.then.33:
  br label %repeat.end.32
if.end.34:
  br label %repeat.cond.31
repeat.cond.31:
  %55 = load i32, i32* @g_i
  %56 = icmp eq i32 %55, 10
  br i1 %56, label %repeat.end.32, label %repeat.body.30
repeat.end.32:
  %57 = load i32, i32* @g_i
  %58 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([16 x i8], [16 x i8]* @.str.10, i32 0, i32 0), i32 %57)
  ret i32 0
}

