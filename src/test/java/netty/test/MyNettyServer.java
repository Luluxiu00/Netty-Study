package netty.test;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class MyNettyServer {
	//服务器地址
    private static final String IP = "127.0.0.1";
    //端口号
    private static final int PORT = 5656;
    //可用的java虚拟机的处理器数*2
    private static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors()*2;
    private static final int BIZTHREADSIZE = 100;
    // 通过nio方式来接收连接和处理连接
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);
    public static void service() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                 pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                    pipeline.addLast(new LengthFieldPrepender(4));
                    pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                    pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                    pipeline.addLast(new TcpServerHandler());
            }
            
        });
        ChannelFuture f = bootstrap.bind(IP, PORT).sync();
        System.out.println("TCP服务器已启动");
        f.channel().closeFuture().sync();
        }

        protected static void shutdown() {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

        public static void main(String[] args) throws Exception {
            System.out.println("开始启动TCP服务器...");
            MyNettyServer.service();
//          HelloServer.shutdown();
        }
}