; ModuleID = 'pascal'
source_filename = "pascal"

declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)
declare i32 @puts(i8*)

@.str.0 = private unnamed_addr constant [11 x i8] c"Inner x = \00"
@.str.1 = private unnamed_addr constant [6 x i8] c"%s%d\0A\00"
@.str.2 = private unnamed_addr constant [23 x i8] c"Inner sees global y = \00"
@.str.3 = private unnamed_addr constant [11 x i8] c"Outer x = \00"
@.str.4 = private unnamed_addr constant [23 x i8] c"Outer x after Inner = \00"
@.str.5 = private unnamed_addr constant [12 x i8] c"Global x = \00"
@.str.6 = private unnamed_addr constant [12 x i8] c"Global y = \00"
@.str.7 = private unnamed_addr constant [2 x i8] c"\0A\00"
@.str.8 = private unnamed_addr constant [24 x i8] c"Global x after calls = \00"
@.str.9 = private unnamed_addr constant [21 x i8] c"=== Loop Scoping ===\00"
@.str.10 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.11 = private unnamed_addr constant [10 x i8] c"Loop x = \00"
@.str.12 = private unnamed_addr constant [16 x i8] c"x after loop = \00"

@g_x = global i32 0
@g_y = global i32 0

define void @p_inner() {
entry:
  %x.addr = alloca i32
  store i32 999, i32* %x.addr
  %0 = load i32, i32* %x.addr
  %1 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([11 x i8], [11 x i8]* @.str.0, i32 0, i32 0), i32 %0)
  %2 = load i32, i32* @g_y
  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([23 x i8], [23 x i8]* @.str.2, i32 0, i32 0), i32 %2)
  ret void
}

define void @p_outer() {
entry:
  %x.addr = alloca i32
  store i32 50, i32* %x.addr
  %0 = load i32, i32* %x.addr
  %1 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([11 x i8], [11 x i8]* @.str.3, i32 0, i32 0), i32 %0)
  call void @p_inner()
  %2 = load i32, i32* %x.addr
  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([23 x i8], [23 x i8]* @.str.4, i32 0, i32 0), i32 %2)
  ret void
}

define i32 @main() {
entry:
  %for.end.0.ptr = alloca i32
  store i32 10, i32* @g_x
  store i32 20, i32* @g_y
  %0 = load i32, i32* @g_x
  %1 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([12 x i8], [12 x i8]* @.str.5, i32 0, i32 0), i32 %0)
  %2 = load i32, i32* @g_y
  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([12 x i8], [12 x i8]* @.str.6, i32 0, i32 0), i32 %2)
  %4 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.7, i32 0, i32 0))
  call void @p_outer()
  %5 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.7, i32 0, i32 0))
  %6 = load i32, i32* @g_x
  %7 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([24 x i8], [24 x i8]* @.str.8, i32 0, i32 0), i32 %6)
  %8 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.7, i32 0, i32 0))
  %9 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.10, i32 0, i32 0), i8* getelementptr inbounds ([21 x i8], [21 x i8]* @.str.9, i32 0, i32 0))
  store i32 100, i32* @g_x
  store i32 3, i32* %for.end.0.ptr
  store i32 1, i32* @g_x
  br label %for.entry.1
for.entry.1:
  %10 = load i32, i32* @g_x
  %11 = load i32, i32* %for.end.0.ptr
  %12 = icmp sle i32 %10, %11
  br i1 %12, label %for.body.2, label %for.end.5
for.body.2:
  %13 = load i32, i32* @g_x
  %14 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([10 x i8], [10 x i8]* @.str.11, i32 0, i32 0), i32 %13)
  br label %for.post.3
for.post.3:
  %15 = load i32, i32* @g_x
  %16 = load i32, i32* %for.end.0.ptr
  %17 = icmp eq i32 %15, %16
  br i1 %17, label %for.end.5, label %for.step.4
for.step.4:
  %18 = add i32 %15, 1
  store i32 %18, i32* @g_x
  br label %for.body.2
for.end.5:
  %19 = load i32, i32* @g_x
  %20 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([16 x i8], [16 x i8]* @.str.12, i32 0, i32 0), i32 %19)
  ret i32 0
}

