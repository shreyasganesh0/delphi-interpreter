; ModuleID = 'pascal'
source_filename = "pascal"

declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)
declare i32 @puts(i8*)

@.str.0 = private unnamed_addr constant [34 x i8] c"=== Constant Propagation Demo ===\00"
@.str.1 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.2 = private unnamed_addr constant [15 x i8] c"2 * (A + B) = \00"
@.str.3 = private unnamed_addr constant [6 x i8] c"%s%d\0A\00"
@.str.4 = private unnamed_addr constant [13 x i8] c"A * B + C = \00"
@.str.5 = private unnamed_addr constant [21 x i8] c"(A + B) * (A - B) = \00"
@.str.6 = private unnamed_addr constant [19 x i8] c"A + B > 15 is True\00"
@.str.7 = private unnamed_addr constant [20 x i8] c"A + B > 15 is False\00"

@g_x = global i32 0

define i32 @main() {
entry:
  %0 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([34 x i8], [34 x i8]* @.str.0, i32 0, i32 0))
  store i32 42, i32* @g_x
  %1 = load i32, i32* @g_x
  %2 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([15 x i8], [15 x i8]* @.str.2, i32 0, i32 0), i32 %1)
  store i32 115, i32* @g_x
  %3 = load i32, i32* @g_x
  %4 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str.4, i32 0, i32 0), i32 %3)
  store i32 -21, i32* @g_x
  %5 = load i32, i32* @g_x
  %6 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([21 x i8], [21 x i8]* @.str.5, i32 0, i32 0), i32 %5)
  br i1 true, label %if.then.0, label %if.else.2
if.then.0:
  %7 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([19 x i8], [19 x i8]* @.str.6, i32 0, i32 0))
  br label %if.end.1
if.else.2:
  %8 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([20 x i8], [20 x i8]* @.str.7, i32 0, i32 0))
  br label %if.end.1
if.end.1:
  ret i32 0
}

