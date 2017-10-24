
package NettyDemo.echo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	/**
	 *此方法会在连接到服务器后被调用 
	 * */
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.write(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
		
		// 注意：需要调用flush将数据发送到服务端  
        ctx.flush();  
	}
	
	/**
	 *此方法会在接收到服务器数据后调用 
	 * */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
		// 读取服务端返回的数据并打印  
		System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
        System.out.println("Client received: " + ByteBufUtil.hexDump(msg.readBytes(msg.readableBytes()))); //转存储后的十六进制
	}
	
	/**
	 *捕捉到异常 
	 * */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}

