package net.luminis.restdemo;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import net.luminis.restdemo.model.Temperature;
import net.luminis.restdemo.model.TemperatureList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Path("/temperature")
public class TemperatureResource {

    private static TemperatureService temperatureService = new TemperatureService();
    private static final JAXBContext context;

    static {
        try {
            context = JAXBContext.newInstance(Temperature.class, TemperatureList.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postJsonReading(String reading) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_XML)
    public Response postXmlReading(String reading) throws URISyntaxException {
        return validatePayload(reading)
                .map(message -> Response.status(Response.Status.BAD_REQUEST).entity(String.format("<error>%s</error>", message)))
                .orElse(storeTemperature(reading))
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getReading(@PathParam("id") String id) {
        try {
            Temperature temperature = temperatureService.getTemperature(id);
            StringWriter stringWriter = new StringWriter();
            context.createMarshaller().marshal(temperature, stringWriter);
            return Response.ok().entity(stringWriter.toString()).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (JAXBException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public Response putReading(@PathParam("id") String id, String newReading) {
        try {
            Temperature temperature = (Temperature) context.createUnmarshaller()
                    .unmarshal(new StringReader(newReading));
            temperatureService.updateTemperature(id, temperature);
            return Response.ok().build();
        } catch (JAXBException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(String.format("<error>%s</error>", e.getMessage())).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_XML)
    public Response getReadings() {
        try {
            List<Temperature> temperatures = temperatureService.getTemperatures();
            StringWriter stringWriter = new StringWriter();
            context.createMarshaller().marshal(new TemperatureList(temperatures), stringWriter);
            return Response.ok().entity(stringWriter.toString()).build();
        } catch (JAXBException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response deleteReading(@PathParam("id") String id) {
        try {
            temperatureService.deleteTemperature(id);
            return Response.ok().build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private Response.ResponseBuilder storeTemperature(String xml) {
        try {
            Temperature temperature = (Temperature) context.createUnmarshaller()
                    .unmarshal(new StringReader(xml));
            String id = temperatureService.storeTemperature(temperature);
            return Response.created(new URI("temperature/" + id));
        } catch (JAXBException | URISyntaxException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Optional<String> validatePayload(String payload) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(this.getClass().getResourceAsStream("/temperature.xsd")));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new ByteArrayInputStream(payload.getBytes())));
        } catch (SAXException | IOException e) {
            return Optional.of(e.getMessage());
        }
        return Optional.empty();
    }

}
