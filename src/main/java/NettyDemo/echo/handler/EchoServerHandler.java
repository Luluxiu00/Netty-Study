
package NettyDemo.echo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
/**
 * Sharable表示此对象在channel间共享
 * handler类是我们的具体业务类
 * */
public class EchoServerHandler extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { 
		ByteBuf buf = (ByteBuf) msg;
		System.out.println("Server received: " + buf.toString(CharsetUtil.UTF_8));
		//缓冲内部存储读写位置，readBytes将指针后移 
        System.out.println("Server received: " + ByteBufUtil.hexDump(buf.readBytes(buf.readableBytes())));//返回指定的缓冲区的字节十六进制转储 
        buf.resetReaderIndex();  //重置读写位置，如果省略这一句，ctx.write(msg)往客户端发送的数据为空  
		ctx.write(msg);//写回数据，
	} 
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) { 
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //flush掉所有写回的数据
		.addListener(ChannelFutureListener.CLOSE); //当flush完成后关闭channel
	} 
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) { 
		cause.printStackTrace();//捕捉异常信息
		ctx.close();//出现异常时关闭channel 
	} 	
}

