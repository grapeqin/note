package grape.learn.netty.protocol.custom;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.serial.SerialMarshallerFactory;

/**
 * Marshaller Provider Utils
 *
 * @author grape
 * @date 2019-06-17
 */
public class MarshallerCodecFactory {

  private static final MarshallerFactory factory = new SerialMarshallerFactory();

  private static final MarshallingConfiguration configuration = new MarshallingConfiguration();

  public static MarshallerProvider createMarshallerProvider() {
    MarshallerProvider provider = new DefaultMarshallerProvider(factory, configuration);
    return provider;
  }

  public static UnmarshallerProvider createUnmarshallerProvider() {
    UnmarshallerProvider provider = new DefaultUnmarshallerProvider(factory, configuration);
    return provider;
  }
}
