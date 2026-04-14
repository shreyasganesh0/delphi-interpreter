; ModuleID = 'pascal'
source_filename = "pascal"

declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)
declare i32 @puts(i8*)

@.str.0 = private unnamed_addr constant [16 x i8] c"Before swap: a=\00"
@.str.1 = private unnamed_addr constant [4 x i8] c" b=\00"
@.str.2 = private unnamed_addr constant [10 x i8] c"%s%d%s%d\0A\00"
@.str.3 = private unnamed_addr constant [16 x i8] c"After swap:  a=\00"
@.str.4 = private unnamed_addr constant [2 x i8] c"\0A\00"
@.str.5 = private unnamed_addr constant [18 x i8] c"Before double: a=\00"
@.str.6 = private unnamed_addr constant [6 x i8] c"%s%d\0A\00"
@.str.7 = private unnamed_addr constant [18 x i8] c"After double:  a=\00"

@g_a = global i32 0
@g_b = global i32 0

define void @p_swap(i32* %arg0, i32* %arg1) {
entry:
  %temp.addr = alloca i32
  %0 = load i32, i32* %arg0
  store i32 %0, i32* %temp.addr
  %1 = load i32, i32* %arg1
  store i32 %1, i32* %arg0
  %2 = load i32, i32* %temp.addr
  store i32 %2, i32* %arg1
  ret void
}

define void @p_doubleit(i32* %arg0) {
entry:
  %0 = load i32, i32* %arg0
  %1 = mul i32 %0, 2
  store i32 %1, i32* %arg0
  ret void
}

define i32 @main() {
entry:
  store i32 10, i32* @g_a
  store i32 20, i32* @g_b
  %0 = load i32, i32* @g_a
  %1 = load i32, i32* @g_b
  %2 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([10 x i8], [10 x i8]* @.str.2, i32 0, i32 0), i8* getelementptr inbounds ([16 x i8], [16 x i8]* @.str.0, i32 0, i32 0), i32 %0, i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i32 %1)
  call void @p_swap(i32* @g_a, i32* @g_b)
  %3 = load i32, i32* @g_a
  %4 = load i32, i32* @g_b
  %5 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([10 x i8], [10 x i8]* @.str.2, i32 0, i32 0), i8* getelementptr inbounds ([16 x i8], [16 x i8]* @.str.3, i32 0, i32 0), i32 %3, i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i32 %4)
  %6 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.4, i32 0, i32 0))
  store i32 7, i32* @g_a
  %7 = load i32, i32* @g_a
  %8 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.6, i32 0, i32 0), i8* getelementptr inbounds ([18 x i8], [18 x i8]* @.str.5, i32 0, i32 0), i32 %7)
  call void @p_doubleit(i32* @g_a)
  %9 = load i32, i32* @g_a
  %10 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.6, i32 0, i32 0), i8* getelementptr inbounds ([18 x i8], [18 x i8]* @.str.7, i32 0, i32 0), i32 %9)
  ret i32 0
}

