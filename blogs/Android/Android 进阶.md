# 启动优化

App 启动分为冷启动、温启动、热启动；冷启动是指 App 进程需要重新开始创建，温启动是指 App 视图已经被回收，但是进程还存活着，热启动是指 App 进程、视图都存活，例如从后台切换至前台。

1. Application 创建回调方法尽量减少耗时操作或者改成异步、延迟操作，例如初始化数据库、SDK 等，多进程 App 根据需要加载第三方 SDK
2. Launch Activity 创建回调方法尽量减少耗时操作或者改成异步、延迟操作，简化视图复杂度，例如减少布局层次。
3. Launch Activity 设置主题背景，提升用户体验

# App 冷启动流程

1. 加载启动App；
2. App启动之后立即展示出一个空白的Window；
3. 创建App的进程；
4. 创建App对象；
5. 启动Main Thread；
6. 创建启动的Activity对象；
7. 加载View；
8. 布置屏幕；
9. 进行第一次绘制；

# 绘制优化

![image-20190620194028803](/Users/lixiongwen/Library/Application Support/typora-user-images/image-20190620194028803.png)

1. 减少不必要的背景
2. 减少嵌套层次
   - 使用 Merge 标签
   - 优先使用 RelativeLayout 等约束布局
3. 减少控件个数
   - 使用 ViewStub 标签
   - 使用 Spannable / Html.fromHtml() 替换多规格 TextView
   - 使用 LinearLayout 自带的分割线
4. 避免在 onDraw() 方法内创建对象，进行复杂运算
5. 使用低端机进行优化，发现性能瓶颈

# 内存抖动

内存抖动是由于短时间内有大量对象进出新生区导致的，它伴随着频繁的GC，gc会大量占用ui线程和cpu资源，会导致app整体卡顿。

避免发生内存抖动的几点建议：

- 尽量避免在循环体内创建对象，应该把对象创建移到循环体外。
- 注意自定义View的onDraw()方法会被频繁调用，所以在这里面不应该频繁的创建对象。
- 当需要大量使用Bitmap的时候，试着把它们缓存在数组或容器中实现复用。
- 对于能够复用的对象，同理可以使用对象池将它们缓存起来。

# 常见内存优化方法

1. 图片优化
   - 尽量选择内存占用少的色彩模式
   - 图片压缩-分辨率
   - 图片压缩-质量
2. 重写 Application 的 onTrimMemory() 方法释放缓存
3. 适当使用弱引用和软引用
4. 尽量采用静态内部类，静态常量
5. onDraw() 尽量避免创建对象

# APK 瘦身

1. 删除 assets 无用资源
2. 动态下载资源
3. 压缩资源文件，使用时再解压
4. 配置 abiFilters
5. 开启混淆
6. 压缩图片
7. 统一风格，重复利用资源
8. 使用 Android 系统资源

## Android 事件分发

![image-20190701205434668](/Users/lixiongwen/Library/Application Support/typora-user-images/image-20190701205434668.png)

# requestLayout 和 invalidate 的区别

1. requestLayout 会回调 View 的 onMeasure()、onLayout()、onDraw() 方法
2. invalidate 只会回调 View 的 onDraw()，postInvalidate 基本一致，但可在非 UI 线程调用

# 进程间通信方式

![image-20190701215049679](/Users/lixiongwen/Library/Application Support/typora-user-images/image-20190701215049679.png)

# Activity 启动流程

![image](https://camo.githubusercontent.com/7abdc45b408bb2f7e65decb20a09c4b336a2b9c0/687474703a2f2f696d672e6d702e6974632e636e2f75706c6f61642f32303137303332392f63613935363763653362663034633461626462346431323463656266656537365f74682e6a706567)

# Android APK 安装大致流程

![image](https://github.com/guoxiaoxing/android-open-source-project-analysis/raw/master/art/app/package/apk_install_structure.png)

# Android APK 打包流程

![image](https://github.com/guoxiaoxing/android-open-source-project-analysis/raw/master/art/native/vm/apk_package_flow.png)

# JVM、Dalvik、ART 虚拟机区别

#### JVM 和Dalvik虚拟机的区别

JVM:.java -> javac -> .class -> jar -> .jar

架构: 堆和栈的架构.

DVM:.java -> javac -> .class -> dx.bat -> .dex

架构: 寄存器(cpu上的一块高速缓存)

#### Android2个虚拟机的区别（一个5.0之前，一个5.0之后）

什么是Dalvik：Dalvik是Google公司自己设计用于Android平台的Java虚拟机。Dalvik虚拟机是Google等厂商合作开发的Android移动设备平台的核心组成部分之一，它可以支持已转换为.dex(即Dalvik Executable)格式的Java应用程序的运行，.dex格式是专为Dalvik应用设计的一种压缩格式，适合内存和处理器速度有限的系统。Dalvik经过优化，允许在有限的内存中同时运行多个虚拟机的实例，并且每一个Dalvik应用作为独立的Linux进程执行。独立的进程可以防止在虚拟机崩溃的时候所有程序都被关闭。

什么是ART:Android操作系统已经成熟，Google的Android团队开始将注意力转向一些底层组件，其中之一是负责应用程序运行的Dalvik运行时。Google开发者已经花了两年时间开发更快执行效率更高更省电的替代ART运行时。ART代表Android Runtime,其处理应用程序执行的方式完全不同于Dalvik，Dalvik是依靠一个Just-In-Time(JIT)编译器去解释字节码。开发者编译后的应用代码需要通过一个解释器在用户的设备上运行，这一机制并不高效，但让应用能更容易在不同硬件和架构上运行。ART则完全改变了这套做法，在应用安装的时候就预编译字节码为机器语言，这一机制叫Ahead-Of-Time(AOT)编译。在移除解释代码这一过程后，应用程序执行将更有效率，启动更快。

ART优点：

- 系统性能的显著提升。
- 应用启动更快、运行更快、体验更流畅、触感反馈更及时。
- 更长的电池续航能力。
- 支持更低的硬件。

ART缺点：

- 更大的存储空间占用，可能会增加10%-20%。
- 更长的应用安装时间。

# 资源文件与资源 ID 映射原理

在编译的时候，AAPT会扫描你所定义的所有资源（在不同文件中定义的以及单独的资源文件），然后给它们指定不同的资源ID。
资源ID 是一个32bit的数字，格式是PPTTNNNN ， PP代表资源所属的包(package) ,TT代表资源的类型(type)，NNNN代表这个类型下面的资源的名称。 对于应用程序的资源来说，PP的取值是0×7f。
TT 和NNNN 的取值是由AAPT工具随意指定的–基本上每一种新的资源类型的数字都是从上一个数字累加的（从1开始）；而每一个新的资源条目也是从数字1开始向上累加的。

注意的是，AAPT在每一次编译的时候不会去保存上一次生成的资源ID标示，每当/res目录发生变化的时候，AAPT可能会去重新给资源指定ID号，然后重新生成一个R.java文件。因此，在做开发的时候，你不应该在程序中将资源ID持久化保存到文件或者数据库。而资源ID在每一次编译后都有可能变化。
一旦资源被编译成二进制文件的时候，AAPT会生成R.java 文件和“resources.arsc”文件，“R.java”用于代码的编译，而”resources.arsc”则包含了全部的资源名称、资源ID和资源的内容（对于单独文件类型的资源，这个内容代表的是这个文件在其.apk 文件中的路径信息）。这样就把运行环境中的资源ID 和具体的资源对应起来了。

# 进程保活

**1）进程拉活：**AIDL方式单进程、双进程方式保活Service（最极端的例子就是推送厂商的互相唤醒复活：极光、友盟、以及各大厂商的推送，同派系APP广播互相唤醒：比如今日头条系、阿里系）；
**2）降低oom_adj的值：**常驻通知栏（可通过启动另外一个服务关闭Notification，不对oom_adj值有影响）、使用”1像素“的Activity覆盖在getWindow()的view上（据传某不可言说的IM大厂用过这个方案，虽然他们从未正面承认过）、循环播放无声音频（黑科技，7.0下杀不掉）；
**3）监听锁屏广播：**使Activity始终保持前台；
**4）使用自定义锁屏界面：**覆盖了系统锁屏界面；
**5）创建子进程：**通过android:process属性来为Service创建一个进程；
**6）白名单：**跳转到系统白名单界面让用户自己添加app进入白名单。

# SurfaceView和View的最本质的区别

SurfaceView是在一个新起的单独线程中可以重新绘制画面，而view必须在UI的主线程中更新画面。

在UI的主线程中更新画面可能会引发问题，比如你更新的时间过长，那么你的主UI线程就会被你正在画的函数阻塞。那么将无法响应按键、触屏等消息。当使用SurfaceView由于是在新的线程中更新画面所以不会阻塞你的UI主线程。但这也带来了另外一个问题，就是事件同步。比如你触屏了一下，你需要在SurfaceView中的thread处理，一般就需要有一个event queue的设计来保存touchevent，这会稍稍复杂一点，因为涉及到线程安全。

# 非UI线程可以更新UI吗

可以，当访问UI时，ViewRootImpl会调用checkThread方法去检查当前访问UI的线程是哪个，如果不是UI线程则会抛出异常。执行onCreate方法的那个时候ViewRootImpl还没创建，无法去检查当前线程.ViewRootImpl的创建在onResume方法回调之后。

```
void checkThread() {
    if (mThread != Thread.currentThread()) {
        throw new CalledFromWrongThreadException(
                "Only the original thread that created a view hierarchy can touch its views.");
    }
}
```

非UI线程是可以刷新UI的，前提是它要拥有自己的ViewRoot,即更新UI的线程和创建ViewRoot的线程是同一个，或者在执行checkThread()前更新UI。

# Android长连接，怎么处理心跳机制

长连接：长连接是建立连接之后, 不主动断开. 双方互相发送数据, 发完了也不主动断开连接, 之后有需要发送的数据就继续通过这个连接发送.

心跳包：其实主要是为了防止NAT超时，客户端隔一段时间就主动发一个数据，探测连接是否断开。

服务器处理心跳包：假如客户端心跳间隔是固定的, 那么服务器在连接闲置超过这个时间还没收到心跳时, 可以认为对方掉线, 关闭连接. 如果客户端心跳会动态改变, 应当设置一个最大值, 超过这个最大值才认为对方掉线. 还有一种情况就是服务器通过TCP连接主动给客户端发消息出现写超时, 可以直接认为对方掉线.