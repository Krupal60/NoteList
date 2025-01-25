# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.note.list.data.local.note.Notes{*;}
-keep class com.note.list.domain.note.Note{*;}
-keep class com.note.list.domain.note.NotesKt{*;}
-keep class com.note.list.data.local.todo.ToDoList{*;}
-keep class com.note.list.domain.todo.ToDoKt{*;}
-keep class com.note.list.domain.todo.ToDo{*;}
-keep class com.note.list.domain.todo.OnToDoAction{*;}
-keep class com.note.list.domain.upsert.OnNoteUpsertAction{*;}
-keep class com.note.list.ui.view.NavItems{*;}
-keep class com.note.list.ui.view.screens.UpsertState{*;}
-keep class com.note.list.ui.view.screens.ToDoState{*;}