# Activity 生命周期

![image-20190618134834011](/Users/lixiongwen/Library/Application Support/typora-user-images/image-20190618134834011.png)

# Fragment 生命周期

![image-20190618135008648](/Users/lixiongwen/Library/Application Support/typora-user-images/image-20190618135008648.png)

# Android 中进程的优先级？

##### 1. 前台进程：

即与用户正在交互的Activity或者Activity用到的Service等，如果系统内存不足时前台进程是最晚被杀死的

##### 2. 可见进程：

可以是处于暂停状态(onPause)的Activity或者绑定在其上的Service，即被用户可见，但由于失了焦点而不能与用户交互

##### 3. 服务进程：

其中运行着使用startService方法启动的Service，虽然不被用户可见，但是却是用户关心的，例如用户正在非音乐界面听的音乐或者正在非下载页面下载的文件等；当系统要空间运行，前两者进程才会被终止

##### 4. 后台进程：

其中运行着执行onStop方法而停止的程序，但是却不是用户当前关心的，例如后台挂着的QQ，这时的进程系统一旦没了有内存就首先被杀死

##### 5. 空进程：

不包含任何应用程序的进程，这样的进程系统是一般不会让他存在的

# Serialzable和Parcelable的区别

![img](https://camo.githubusercontent.com/2a7f3963c1e23c9907eb516227ee51f60e9b00c0/68747470733a2f2f757365722d676f6c642d63646e2e786974752e696f2f323031392f332f382f313639356333343966303139633431663f696d616765736c696d)

# Android 新特性

##### Android8.0（O）新特性

- **优化通知**

  通知渠道 (Notification Channel) 通知标志 休眠 通知超时 通知设置 通知清除

- **画中画模式**：清单中Activity设置android:supportsPictureInPicture

- **后台限制**

- 自动填充框架

- 系统优化

- 等等优化很多

##### Android9.0（P）新特性

- **室内WIFI定位**
- **“刘海”屏幕支持**
- 安全增强
- 等等优化很多

##### Android10.0（Q）目前曝光的新特性

- **夜间模式**：包括手机上的所有应用都可以为其设置暗黑模式。
- **桌面模式**：提供类似于PC的体验，但是远远不能代替PC。
- **屏幕录制**：通过长按“电源”菜单中的"屏幕快照"来开启。

# Merge、ViewStub 的作用

Merge: 减少视图层级，可以删除多余的层级。

ViewStub: 按需加载，减少内存使用量、加快渲染速度、不支持 merge 标签。

# Asset目录与res目录的区别

assets：不会在 R 文件中生成相应标记，存放到这里的资源在打包时会打包到程序安装包中。（通过 AssetManager 类访问这些文件）

res：会在 R 文件中生成 id 标记，资源在打包时如果使用到则打包到安装包中，未用到不会打入安装包中。

res/anim：存放动画资源。

res/raw：和 asset 下文件一样，打包时直接打入程序安装包中（会映射到 R 文件中）。

# Android 启动优化

1. 利用提前展示出来的Window，快速展示出来一个界面，给用户快速反馈的体验
2. 避免在启动时做密集沉重的初始化（Heavy app initialization）
3. 定位问题：避免I/O操作、反序列化、网络操作、布局嵌套等。

# Handler 机制

![image-20190618162427202](/Users/lixiongwen/Library/Application Support/typora-user-images/image-20190618162427202.png)

- 1、Handler通过sendMessage()发送消息Message到消息队列MessageQueue。
- 2、Looper通过loop()不断提取触发条件的Message，并将Message交给对应的target handler来处理。
- 3、target handler调用自身的handleMessage()方法来处理Message。

# Handler 引起的内存泄露原因以及最佳解决方案

Handler 允许我们发送延时消息，如果在延时期间用户关闭了 Activity，那么该 Activity 会泄露。 这个泄露是因为 Message 会持有 Handler，而又因为 Java 的特性，内部类会持有外部类，使得 Activity 会被 Handler 持有，这样最终就导致 Activity 泄露。

解决：将 Handler 定义成静态的内部类，在内部持有 Activity 的弱引用，并在Acitivity的onDestroy()中调用 handler.removeCallbacksAndMessages(null) 及时移除所有消息。

# 为什么我们能在主线程直接使用 Handler，而不需要创建 Looper 

通常我们认为 ActivityThread 就是主线程。事实上它并不是一个线程，而是主线程操作的管理者。在 ActivityThread.main() 方法中调用了 Looper.prepareMainLooper() 方法创建了 主线程的 Looper ,并且调用了 loop() 方法，所以我们就可以直接使用 Handler 了。

因此我们可以利用 Callback 这个拦截机制来拦截 Handler 的消息。如大部分插件化框架中Hook ActivityThread.mH 的处理。

##### 主线程的 Looper 不允许退出

主线程不允许退出，退出就意味 APP 要挂。

# 创建 Message 实例的最佳方式

为了节省开销，Android 给 Message 设计了回收机制，所以我们在使用的时候尽量复用 Message ，减少内存消耗：

- 通过 Message 的静态方法 Message.obtain()；
- 通过 Handler 的公有方法 handler.obtainMessage()。

# 内存泄漏原因及解决方案

1. 静态变量内存泄漏（单例）

   静态特性使对象的生命周期和应用的生命周期一样长，如果静态变量持有生命周期较短的对象的引用则造成内存泄漏。

   例如：非静态内部类默认会持有外部类的引用，而该非静态内部类又创建了一个静态的实例，该实例的生命周期和应用的一样长，这就导致了该静态实例一直会持有该Activity的引用，从而导致Activity的内存资源不能被正常回收

   解决方案：将该内部类设为静态内部类或将该内部类抽取出来封装成一个单例，如果需要使用 Context，尽量使用 Application的Context

2. Handler 内存泄漏

   非静态内部类和匿名类内部类都会潜在持有它们所属的外部类的引用，Handler 的 MessageQueue 如果还有未处理完成消息，则会一直持有 Activity 的引用，导致 Activity 不能被释放回收，造成内存泄漏

   解决方案：使用继承 Handler 的静态内部类，持有外部 Activity 的弱引用，并且在 Activity 销毁时调用 handler.removeCallbacksAndMessages(null)。 

3. 线程内存泄漏

   非静态内部类和匿名类内部类创建线程对象也会持有 Activity 的引用，如果 Activity 销毁时线程还在运行，则会导致内存泄漏，处理方式和 Handler 一致

4. 资源未关闭、回收、反注册

   BraodcastReceiver，ContentObserver，File，Cursor，Stream，Bitmap等资源，应该在Activity销毁时及时关闭或者注销

5. WebView 内存泄露
6. 集合类内存泄漏

# 强引用置为null，会不会被回收

不会立即释放对象占用的内存。 如果对象的引用被置为null，只是断开了当前线程栈帧中对该对象的引用关系，而 垃圾收集器是运行在后台的线程，只有当用户线程运行到安全点(safe point)或者安全区域才会扫描对象引用关系，扫描到对象没有被引用则会标记对象，这时候仍然不会立即释放该对象内存，因为有些对象是可恢复的（在 finalize方法中恢复引用 ）。只有确定了对象无法恢复引用的时候才会清除对象内存。

# Bundle传递数据为什么需要序列化

序列化，表示将一个对象转换成可存储或可传输的状态。序列化的原因基本三种情况：

1.永久性保存对象，保存对象的字节序列到本地文件中；

2.对象在网络中传递；

3.对象在IPC间传递。

# LaunchMode应用场景

standard，创建一个新的Activity。

singleTop，栈顶不是该类型的Activity，创建一个新的Activity。否则，onNewIntent。

singleTask，回退栈中没有该类型的Activity，创建Activity，否则，onNewIntent+ClearTop。

singleInstance，回退栈中，只有这一个Activity，没有其他Activity。

# BroadcastReceiver，LocalBroadcastReceiver 区别

##### 1、应用场景

   1、BroadcastReceiver用于应用之间的传递消息；

   2、而LocalBroadcastManager用于应用内部传递消息，比broadcastReceiver更加高效。

##### 2、安全

   1、BroadcastReceiver使用的Content API，所以本质上它是跨应用的，所以在使用它时必须要考虑到不要被别的应用滥用；

   2、LocalBroadcastManager不需要考虑安全问题，因为它只在应用内部有效。

##### 3、原理方面

(1) 与BroadcastReceiver是以 Binder 通讯方式为底层实现的机制不同，LocalBroadcastManager 的核心实现实际还是 Handler，只是利用到了 IntentFilter 的 match 功能，至于 BroadcastReceiver 换成其他接口也无所谓，顺便利用了现成的类和概念而已。

(2) LocalBroadcastManager因为是 Handler 实现的应用内的通信，自然安全性更好，效率更高。

# 如何保证Service不被杀死？

Android 进程不死从3个层面入手：

A.提供进程优先级，降低进程被杀死的概率

方法一：监控手机锁屏解锁事件，在屏幕锁屏时启动1个像素的 Activity，在用户解锁时将 Activity 销毁掉。

方法二：启动前台service。

方法三：提升service优先级：

在AndroidManifest.xml文件中对于intent-filter可以通过android:priority = "1000"这个属性设置最高优先级，1000是最高值，如果数字越小则优先级越低，同时适用于广播。

B. 在进程被杀死后，进行拉活

方法一：注册高频率广播接收器，唤起进程。如网络变化，解锁屏幕，开机等

方法二：双进程相互唤起。

方法三：依靠系统唤起。

方法四：onDestroy方法里重启service：service + broadcast 方式，就是当service走ondestory的时候，发送一个自定义的广播，当收到广播的时候，重新启动service；

C. 依靠第三方

根据终端不同，在小米手机（包括 MIUI）接入小米推送、华为手机接入华为推送；其他手机可以考虑接入腾讯信鸽或极光推送与小米推送做 A/B Test。

# View.post() 和 Handler.post() 的区别

1. View.post() 肯定是在主线程执行的，Handler.post() 是在 Handler 所在的线程执行
2. View.post() 如果 View 还没 attach 到 Window 时，Runnable 会放到 ViewRootImpl 的 RunQueue 中，等到下次 ViewRootImpl 的下一个performTraversals 时候，把 RunQueue 里的所有 Runnable 都拿出来并执行，接着清空 RunQueue，其他情况 Handler.post() 和 View.post() 都是添加到 MessageQueue 中

# IntentService

IntentService是一种特殊的Service，它继承了Service并且它是一个抽象类，因此必须创建它的子类才能使用IntentService。

##### 原理

在实现上，IntentService封装了HandlerThread和Handler。当IntentService被第一次启动时，它的onCreate()方法会被调用，onCreat()方法会创建一个HandlerThread，然后使用它的Looper来构造一个Handler对象mServiceHandler，这样通过mServiceHandler发送的消息最终都会在HandlerThread中执行。

生成一个默认的且与主线程互相独立的工作者线程来执行所有传送至onStartCommand()方法的Intetnt。

生成一个工作队列来传送Intent对象给onHandleIntent()方法，同一时刻只传送一个Intent对象，这样一来，你就不必担心多线程的问题。在所有的请求(Intent)都被执行完以后会自动停止服务，所以，你不需要自己去调用stopSelf()方法来停止。

该服务提供了一个onBind()方法的默认实现，它返回null。

提供了一个onStartCommand()方法的默认实现，它将Intent先传送至工作队列，然后从工作队列中每次取出一个传送至onHandleIntent()方法，在该方法中对Intent做相应的处理。 