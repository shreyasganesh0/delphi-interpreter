; ModuleID = 'pascal'
source_filename = "pascal"

declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)
declare i32 @puts(i8*)

@.str.0 = private unnamed_addr constant [18 x i8] c"Hello, Constants!\00"
@.str.1 = private unnamed_addr constant [4 x i8] c"%s\0A\00"
@.str.2 = private unnamed_addr constant [12 x i8] c"MAX_SIZE = \00"
@.str.3 = private unnamed_addr constant [6 x i8] c"%s%d\0A\00"
@.str.4 = private unnamed_addr constant [13 x i8] c"PI_APPROX = \00"
@.str.5 = private unnamed_addr constant [38 x i8] c"Approximate area of circle with r=5: \00"
@.str.6 = private unnamed_addr constant [21 x i8] c"MAX_SIZE + OFFSET = \00"
@.str.7 = private unnamed_addr constant [2 x i8] c"\0A\00"
@.str.8 = private unnamed_addr constant [27 x i8] c"=== Constants in loops ===\00"
@.str.9 = private unnamed_addr constant [3 x i8] c"r=\00"
@.str.10 = private unnamed_addr constant [8 x i8] c" area~=\00"
@.str.11 = private unnamed_addr constant [10 x i8] c"%s%d%s%d\0A\00"

@g_area = global i32 0
@g_radius = global i32 0

define i32 @main() {
entry:
  %for.end.0.ptr = alloca i32
  %0 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([18 x i8], [18 x i8]* @.str.0, i32 0, i32 0))
  %1 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([12 x i8], [12 x i8]* @.str.2, i32 0, i32 0), i32 100)
  %2 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str.4, i32 0, i32 0), i32 3)
  store i32 5, i32* @g_radius
  %3 = load i32, i32* @g_radius
  %4 = load i32, i32* @g_radius
  %5 = mul i32 %3, %4
  %6 = mul i32 3, %5
  store i32 %6, i32* @g_area
  %7 = load i32, i32* @g_area
  %8 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([38 x i8], [38 x i8]* @.str.5, i32 0, i32 0), i32 %7)
  %9 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([21 x i8], [21 x i8]* @.str.6, i32 0, i32 0), i32 110)
  %10 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.7, i32 0, i32 0))
  %11 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([27 x i8], [27 x i8]* @.str.8, i32 0, i32 0))
  store i32 3, i32* %for.end.0.ptr
  store i32 1, i32* @g_radius
  br label %for.entry.1
for.entry.1:
  %12 = load i32, i32* @g_radius
  %13 = load i32, i32* %for.end.0.ptr
  %14 = icmp sle i32 %12, %13
  br i1 %14, label %for.body.2, label %for.end.5
for.body.2:
  %15 = load i32, i32* @g_radius
  %16 = load i32, i32* @g_radius
  %17 = load i32, i32* @g_radius
  %18 = mul i32 %16, %17
  %19 = mul i32 3, %18
  %20 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([10 x i8], [10 x i8]* @.str.11, i32 0, i32 0), i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.9, i32 0, i32 0), i32 %15, i8* getelementptr inbounds ([8 x i8], [8 x i8]* @.str.10, i32 0, i32 0), i32 %19)
  br label %for.post.3
for.post.3:
  %21 = load i32, i32* @g_radius
  %22 = load i32, i32* %for.end.0.ptr
  %23 = icmp eq i32 %21, %22
  br i1 %23, label %for.end.5, label %for.step.4
for.step.4:
  %24 = add i32 %21, 1
  store i32 %24, i32* @g_radius
  br label %for.body.2
for.end.5:
  ret i32 0
}

