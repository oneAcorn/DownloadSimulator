以生产者消费者并发模型模拟爬取漫画图片,帮此漫画app:[点击下载](https://github.com/warriorWorld/MangaReader/raw/master/app/release/app-release.apk "Android apk 安装包")实现的下载,这个漫画app主要用于学习英语的,全是英文漫画.

ps:这个app不是我开发的,我只是帮忙,[漫画项目链接](https://github.com/warriorWorld/MangaReader "漫画项目链接")

##### 首先是生产者消费者的总体模型,如图:
![github](https://github.com/oneAcorn/DownloadSimulator/blob/master/img/producer-consumer.png)

总体来说就是用阻塞队列实现的仓库解耦生产者和消费者,使生产者和消费者都可以放心生产或消费,不用管对方的状态

##### 生产者实现方式,如图:
![github](https://github.com/oneAcorn/DownloadSimulator/blob/master/img/producer.jpg)

生产者流程较多,因为需要考虑下载失败的情况,生产者线程会在解析完自己负责的所有章节后,把所有下载失败的的Page放入一个环链表,之后轮询环链表,成功的或已达最大重试次数的节点删掉,失败的节点再次放入阻塞队列.最后环链表为空后通知Dispatcher下载结束,关闭线程,Dispatcher再根据情况决定是否开启一个消费者线程.

##### 消费者实现方式,如图:
![github](https://github.com/oneAcorn/DownloadSimulator/blob/master/img/consumer.jpg)

消费者很简单,只需要下载图片后标记是否成功的状态即可,之后由生产者根据状态决定是否再次放入仓库,注:不能由消费者来放入仓库,因为极端情况下,会导致所有消费者和生产者都在试图把Page放入仓库,仓库阻塞后,因为没有人消费导致永远阻塞下去.
