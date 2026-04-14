; ModuleID = 'pascal'
source_filename = "pascal"

declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)
declare i32 @puts(i8*)

@.str.0 = private unnamed_addr constant [25 x i8] c"=== While with Break ===\00"
@.str.1 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.2 = private unnamed_addr constant [12 x i8] c"Sum 1..5 = \00"
@.str.3 = private unnamed_addr constant [6 x i8] c"%s%d\0A\00"
@.str.4 = private unnamed_addr constant [28 x i8] c"=== While with Continue ===\00"
@.str.5 = private unnamed_addr constant [19 x i8] c"Sum of odd 1..9 = \00"

@g_i = global i32 0
@g_sum = global i32 0

define i32 @main() {
entry:
  %0 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([25 x i8], [25 x i8]* @.str.0, i32 0, i32 0))
  store i32 1, i32* @g_i
  store i32 0, i32* @g_sum
  br label %while.cond.0
while.cond.0:
  %1 = load i32, i32* @g_i
  %2 = icmp sle i32 %1, 100
  br i1 %2, label %while.body.1, label %while.end.2
while.body.1:
  %3 = load i32, i32* @g_i
  %4 = icmp sgt i32 %3, 5
  br i1 %4, label %if.then.3, label %if.end.4
if.then.3:
  br label %while.end.2
if.end.4:
  %5 = load i32, i32* @g_sum
  %6 = load i32, i32* @g_i
  %7 = add i32 %5, %6
  store i32 %7, i32* @g_sum
  %8 = load i32, i32* @g_i
  %9 = add i32 %8, 1
  store i32 %9, i32* @g_i
  br label %while.cond.0
while.end.2:
  %10 = load i32, i32* @g_sum
  %11 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([12 x i8], [12 x i8]* @.str.2, i32 0, i32 0), i32 %10)
  %12 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([28 x i8], [28 x i8]* @.str.4, i32 0, i32 0))
  store i32 0, i32* @g_i
  store i32 0, i32* @g_sum
  br label %while.cond.5
while.cond.5:
  %13 = load i32, i32* @g_i
  %14 = icmp slt i32 %13, 10
  br i1 %14, label %while.body.6, label %while.end.7
while.body.6:
  %15 = load i32, i32* @g_i
  %16 = add i32 %15, 1
  store i32 %16, i32* @g_i
  %17 = load i32, i32* @g_i
  %18 = srem i32 %17, 2
  %19 = icmp eq i32 %18, 0
  br i1 %19, label %if.then.8, label %if.end.9
if.then.8:
  br label %while.cond.5
if.end.9:
  %20 = load i32, i32* @g_sum
  %21 = load i32, i32* @g_i
  %22 = add i32 %20, %21
  store i32 %22, i32* @g_sum
  br label %while.cond.5
while.end.7:
  %23 = load i32, i32* @g_sum
  %24 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([19 x i8], [19 x i8]* @.str.5, i32 0, i32 0), i32 %23)
  ret i32 0
}

