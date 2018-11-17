package mitake;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

	public void connect(int port, String host) throws Exception {
		// ���ÿͻ���NIO�߳���
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new TimeClientHandler());
						}
					});
			// �����첽���Ӳ���
			ChannelFuture f = b.connect(host, port).sync();
			// �ȴ��ͻ�����·�ر�
			f.channel().closeFuture().sync();
		} finally {
			// �����˳�,�ͷ�NIO�߳���
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 12345;
		new TimeClient().connect(port, "127.0.0.1");
	}
}
