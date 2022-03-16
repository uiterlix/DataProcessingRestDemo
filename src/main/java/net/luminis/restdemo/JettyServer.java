package net.luminis.restdemo;

import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class JettyServer {

    public static void main(String[] args) throws Exception {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(8090).build();
        ResourceConfig config = new ResourceConfig(TemperatureResource.class);
        Server server = JettyHttpContainerFactory.createServer(baseUri, config);
        server.start();
    }
}
