package org.regenstrief.ohie;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.dcm4chee.xds2.infoset.ihe.ProvideAndRegisterDocumentSetRequestType;
import org.dcm4chee.xds2.infoset.ihe.RetrieveDocumentSetRequestType;
import org.dcm4chee.xds2.infoset.ihe.RetrieveDocumentSetResponseType;
import org.dcm4chee.xds2.infoset.rim.AdhocQueryRequest;
import org.dcm4chee.xds2.infoset.rim.AdhocQueryResponse;
import org.dcm4chee.xds2.infoset.rim.RegistryResponseType;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

/**
 * OpenHIE E2E tests
 * @author Justin
 *
 */
public class OhieE2eTest {

    /**
     * Test basic case
     * @throws IOException
     * @throws HL7Exception
     * @throws LLPException
     * @throws JAXBException
     */


    @Test
    public void testSuccessfulPOST() throws HL7Exception, IOException, LLPException, JAXBException {

         String documentId = String.format("1.3.6.1.4.1.21367.2010.1.2.%s", new SimpleDateFormat("HHmmss").format(new Date()));
         ModifyXDSbMessage modify = new ModifyXDSbMessage();
         modify.modify("OHIE-XDS-01-30.xml");
         ProvideAndRegisterDocumentSetRequestType pnrRequest = XdsMessageUtil.loadMessage("OHIE-XDS-01-30", ProvideAndRegisterDocumentSetRequestType.class);
         RegistryResponseType xdsResponse = XdsMessageUtil.provideAndRegister(pnrRequest);
         XdsMessageUtil.assertSuccess(xdsResponse);

    }


    @Test
    public void testPOSTWithInvalidPatient() throws HL7Exception, IOException, LLPException, JAXBException {

        String documentId = String.format("1.3.6.1.4.1.21367.2010.1.2.%s", new SimpleDateFormat("HHmmss").format(new Date()));
        ModifyXDSbMessage modify = new ModifyXDSbMessage();
        modify.modify("OHIE-XDS-01-60.xml");
        ProvideAndRegisterDocumentSetRequestType pnrRequest = XdsMessageUtil.loadMessage("OHIE-XDS-01-60", ProvideAndRegisterDocumentSetRequestType.class);
        RegistryResponseType xdsResponse = XdsMessageUtil.provideAndRegister(pnrRequest);
        System.out.println(xdsResponse.getStatus());
        //XdsMessageUtil.assertSuccess(xdsResponse);

    }

    @Test
    public void testPOSTWithInvalidProvider() throws HL7Exception, IOException, LLPException, JAXBException {

        String documentId = String.format("1.3.6.1.4.1.21367.2010.1.2.%s", new SimpleDateFormat("HHmmss").format(new Date()));
        ModifyXDSbMessage modify = new ModifyXDSbMessage();
        modify.modify("OHIE-XDS-01-60.xml");
        ProvideAndRegisterDocumentSetRequestType pnrRequest = XdsMessageUtil.loadMessage("OHIE-XDS-01-60", ProvideAndRegisterDocumentSetRequestType.class);
        RegistryResponseType xdsResponse = XdsMessageUtil.provideAndRegister(pnrRequest);
        System.out.println(xdsResponse.getStatus());
        //XdsMessageUtil.assertSuccess(xdsResponse);

    }

    @Test
    public void testSuccessfulQuery() throws HL7Exception, IOException, LLPException, JAXBException{
        // STEP 40 - Load the XDS query and perform a query

         AdhocQueryRequest queryRequest = XdsMessageUtil.loadMessage("OHIE-XDS-01-20", AdhocQueryRequest.class);
         AdhocQueryResponse queryResponse = XdsMessageUtil.registryStoredQuery(queryRequest);
         XdsMessageUtil.assertSuccess(queryResponse);
         System.out.println(queryResponse);
         XdsMessageUtil.assertHasDocumentId(queryResponse, "2.16.840.1.113883.3.72.5.9.1.0.5386503746339693382111111111");

         // STEP 50 - Retrieve
         RetrieveDocumentSetRequestType retrieveRequest =  XdsMessageUtil.createRetrieveRequest("2.16.840.1.113883.3.72.5.9.1.0.5386503746339693382111111111", queryResponse);
         RetrieveDocumentSetResponseType retrieveResponse = XdsMessageUtil.retrieveDocumentSet(retrieveRequest);
         assertEquals(1, retrieveResponse.getDocumentResponse().size());
         XdsMessageUtil.assertMatchesHash(queryResponse, retrieveResponse);

    }

    @Test
    public void testSuccessfulMultiplePOST() throws HL7Exception, IOException, LLPException, JAXBException {

        String documentId = String.format("1.3.6.1.4.1.21367.2010.1.2.%s", new SimpleDateFormat("HHmmss").format(new Date()));
        ModifyMultipleXDSbMessage modify = new ModifyMultipleXDSbMessage();
        modify.modify("OHIE-XDS-01-80.xml");
        ProvideAndRegisterDocumentSetRequestType pnrRequest = XdsMessageUtil.loadMessage("OHIE-XDS-01-80", ProvideAndRegisterDocumentSetRequestType.class);
        RegistryResponseType xdsResponse = XdsMessageUtil.provideAndRegister(pnrRequest);
        XdsMessageUtil.assertSuccess(xdsResponse);

    }

    @Test
    public void testSuccessfulMultipleQuery() throws HL7Exception, IOException, LLPException, JAXBException{

        // STEP 40 - Load the XDS query and perform a query

        AdhocQueryRequest queryRequest = XdsMessageUtil.loadMessage("OHIE-XDS-01-90", AdhocQueryRequest.class);
        //XdsMessageUtil.updateAdhocQueryRequest(queryRequest, assertTerser.getSegment("/QUERY_RESPONSE(0)/PID"));
        AdhocQueryResponse queryResponse = XdsMessageUtil.registryStoredQuery(queryRequest);
        XdsMessageUtil.assertSuccess(queryResponse);
        XdsMessageUtil.assertHasDocumentId(queryResponse, "2");

        // STEP 50 - Retrieve
        /**RetrieveDocumentSetRequestType retrieveRequest =  XdsMessageUtil.createRetrieveRequest(documentId, queryResponse);
        RetrieveDocumentSetResponseType retrieveResponse = XdsMessageUtil.retrieveDocumentSet(retrieveRequest);
        assertEquals(1, retrieveResponse.getDocumentResponse().size());
        XdsMessageUtil.assertMatchesHash(queryResponse, retrieveResponse);**/

    }




}
