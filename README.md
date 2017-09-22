# LeftSwipeLayout
左滑控件,类似QQ左滑出现删除按钮；
逻辑和功能较简单，只是继承LinearLayout重写onTouchEvent做了点事件处理。

使用方法：
  在LeftSwipeLayout中放两个子View，第一个子View的宽度设置成match_parent，并将LeftSwipeLayout的布局方向设成horizontal，就能有左滑的功能了；
  若需要手动调用方法来展开或关闭第二个布局（左滑出现的菜单布局），分别是调用LeftSwipeLayout的open()和close()方法。
