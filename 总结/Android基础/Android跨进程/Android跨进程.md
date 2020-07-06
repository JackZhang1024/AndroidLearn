- [Android跨进程](#android跨进程)
  - [文件](#文件)
  - [Socket](#socket)
  - [Messager](#messager)
  - [AIDL](#aidl)
  - [ContentProvider](#contentprovider)
## Android跨进程

Android跨进程通信的方式有文件，Socket, Messager, AIDL, ContentProvider。文件因为存在同时读写的问题，所以比较适合对实时性要求不高的应用场景，Socket本来最常见的是跨机器间通信，但是也可以用于不同进程的通信，但是存在安全问题，需要校验身份的时候，比较麻烦，AIDL和Messager其实是同一种方式，利用Binder机制来实现跨进程通信，比较安全，是Android区别于Linux的跨进程方式，ContentProvider也是一种跨进程的通信方式，但是一般用于不同的应用之间的数据库访问。

### 文件

### Socket

### Messager

### AIDL 

### ContentProvider



