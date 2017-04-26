package nl.rug.search.ssap;

import nl.rug.search.ssap.model.Instance;
import nl.rug.search.ssap.model.Pattern;
import nl.rug.search.ssap.model.Role;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by feitosadaniel on 24/03/2017.
 */
public class XMLUtilTest {

    private static final nl.rug.search.ssap.model.System testSystem = createTestSystem();

    private static nl.rug.search.ssap.model.System createTestSystem() {
        nl.rug.search.ssap.model.System s = new nl.rug.search.ssap.model.System();
        s.setPatterns(new ArrayList<>());

        Pattern p;
        Instance i;
        Role r;
        List<Role> rList;

        p = new Pattern();
        s.getPatterns().add(p);
        p.setName("Factory Method");
        p.setInstances(new ArrayList<>());
            i = new Instance();
            p.getInstances().add(i);
            rList = new ArrayList<>();
            i.setRoles(rList);
                r = new Role();
                rList.add(r);
                r.setName("Creator");
                r.setElement("org.bonitasoft.engine.actor.mapping.model.SActorBuilderFactory");
                r = new Role();
                rList.add(r);
                r.setName("FactoryMethod()");
                r.setElement("org.bonitasoft.engine.actor.mapping.model.SActorBuilderFactory::create(java.lang.String, long, boolean):org.bonitasoft.engine.actor.mapping.model.SActorBuilder");
                r = new Role();
                rList.add(r);
                r.setName("ConcreteCreator");
                r.setElement("org.bonitasoft.engine.actor.mapping.model.impl.SActorBuilderFactoryImpl");
                r = new Role();
                rList.add(r);
                r.setName("Product");
                r.setElement("org.bonitasoft.engine.actor.mapping.model.SActorBuilder");
            i = new Instance();
            p.getInstances().add(i);
            rList = new ArrayList<>();
            i.setRoles(rList);
                r = new Role();
                rList.add(r);
                r.setName("Creator");
                r.setElement("org.bonitasoft.engine.api.PlatformLoginAPI");
                r = new Role();
                rList.add(r);
                r.setName("FactoryMethod()");
                r.setElement("org.bonitasoft.engine.api.PlatformLoginAPI::login(java.lang.String, java.lang.String):org.bonitasoft.engine.session.PlatformSession");
                r = new Role();
                rList.add(r);
                r.setName("ConcreteCreator");
                r.setElement("org.bonitasoft.engine.api.impl.PlatformLoginAPIImpl");
                r = new Role();
                rList.add(r);
                r.setName("Product");
                r.setElement("org.bonitasoft.engine.session.PlatformSession");
        p = new Pattern();
        s.getPatterns().add(p);
        p.setName("Prototype");
        p.setInstances(new ArrayList<>());
        p = new Pattern();
        s.getPatterns().add(p);
        p.setName("Singleton");
        p.setInstances(new ArrayList<>());
            i = new Instance();
            p.getInstances().add(i);
            rList = new ArrayList<>();
            i.setRoles(rList);
                r = new Role();
                rList.add(r);
                r.setName("Singleton");
                r.setElement("org.bonitasoft.console.common.server.preferences.properties.PlatformTenantConfigProperties");
                r = new Role();
                rList.add(r);
                r.setName("uniqueInstance");
                r.setElement("private static org.bonitasoft.console.common.server.preferences.properties.PlatformTenantConfigProperties instance");
            i = new Instance();
            p.getInstances().add(i);
            rList = new ArrayList<>();
            i.setRoles(rList);
                r = new Role();
                rList.add(r);
                r.setName("Singleton");
                r.setElement("org.bonitasoft.console.common.server.sso.InternalSSOManager");
                r = new Role();
                rList.add(r);
                r.setName("uniqueInstance");
                r.setElement("private static org.bonitasoft.console.common.server.sso.InternalSSOManager instance");
        p = new Pattern();
        s.getPatterns().add(p);
        p.setName("(Object)Adapter-Command");
        p.setInstances(new ArrayList<>());
            i = new Instance();
            p.getInstances().add(i);
            rList = new ArrayList<>();
            i.setRoles(rList);
                r = new Role();
                rList.add(r);
                r.setName("Adaptee/Receiver");
                r.setElement("org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClientCreator");
                r = new Role();
                rList.add(r);
                r.setName("Adapter/ConcreteCommand");
                r.setElement("org.bonitasoft.web.rest.server.api.organization.APICustomUserInfoDefinition");
                r = new Role();
                rList.add(r);
                r.setName("Request()/Execute()");
                r.setElement("org.bonitasoft.web.rest.server.api.organization.APICustomUserInfoDefinition::delete(java.util.List):void");
            i = new Instance();
            p.getInstances().add(i);
            rList = new ArrayList<>();
            i.setRoles(rList);
                r = new Role();
                rList.add(r);
                r.setName("Adaptee/Receiver");
                r.setElement("org.bonitasoft.engine.actor.xml.Actor");
                r = new Role();
                rList.add(r);
                r.setName("Adapter/ConcreteCommand");
                r.setElement("org.bonitasoft.engine.actor.xml.ActorBinding");
                r = new Role();
                rList.add(r);
                r.setName("Request()/Execute()");
                r.setElement("org.bonitasoft.engine.actor.xml.ActorBinding::setAttributes(java.util.Map):void");
                r = new Role();
                rList.add(r);
                r.setName("Request()/Execute()");
                r.setElement("org.bonitasoft.engine.actor.xml.ActorBinding::setChildObject(java.lang.String, java.lang.Object):void");
        p = new Pattern();
        s.getPatterns().add(p);
        p.setName("Composite");
        p.setInstances(new ArrayList<>());
        p = new Pattern();
        s.getPatterns().add(p);
        p.setName("State-Strategy");
        p.setInstances(new ArrayList<>());
            i = new Instance();
            p.getInstances().add(i);
            rList = new ArrayList<>();
            i.setRoles(rList);
                r = new Role();
                rList.add(r);
                r.setName("Context");
                r.setElement("org.bonitasoft.web.rest.server.datastore.bpm.flownode.TaskFinder");
                r = new Role();
                rList.add(r);
                r.setName("State/Strategy");
                r.setElement("org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet");
                r = new Role();
                rList.add(r);
                r.setName("ConcreteState/Strategy");
                r.setElement("org.bonitasoft.web.rest.server.datastore.profile.GetBonitaPageHelper");
                r = new Role();
                rList.add(r);
                r.setName("ConcreteState/Strategy");
                r.setElement("org.bonitasoft.web.rest.server.datastore.ComposedDatastore");
                r = new Role();
                rList.add(r);
                r.setName("ConcreteState/Strategy");
                r.setElement("org.bonitasoft.web.rest.server.api.bpm.flownode.AbstractAPIFlowNode$1");
                r = new Role();
                rList.add(r);
                r.setName("ConcreteState/Strategy");
                r.setElement("org.bonitasoft.web.rest.server.datastore.organization.GroupDatastore");
                r = new Role();
                rList.add(r);
                r.setName("ConcreteState/Strategy");
                r.setElement("org.bonitasoft.web.rest.server.datastore.bpm.flownode.AbstractFlowNodeDatastore");
                r = new Role();
                rList.add(r);
                r.setName("ConcreteState/Strategy");
                r.setElement("org.bonitasoft.web.rest.server.datastore.bpm.cases.ArchivedCaseDatastore");
                r = new Role();
                rList.add(r);
                r.setName("ConcreteState/Strategy");
                r.setElement("org.bonitasoft.web.rest.server.datastore.organization.RoleDatastore");
            i = new Instance();
            p.getInstances().add(i);
            rList = new ArrayList<>();
            i.setRoles(rList);
                r = new Role();
                rList.add(r);
                r.setName("Context");
                r.setElement("org.bonitasoft.engine.execution.work.ExecuteConnectorWork$EvaluateConnectorOutputsTxContent");
                r = new Role();
                rList.add(r);
                r.setName("State/Strategy");
                r.setElement("org.bonitasoft.engine.execution.work.ExecuteConnectorWork");
                r = new Role();
                rList.add(r);
                r.setName("ConcreteState/Strategy");
                r.setElement("org.bonitasoft.engine.execution.work.ExecuteConnectorOfProcess");
                r = new Role();
                rList.add(r);
                r.setName("ConcreteState/Strategy");
                r.setElement("org.bonitasoft.engine.execution.work.ExecuteConnectorOfActivity");

        return s;
    }

    private static final String testString = "<?xml version='1.0' encoding='UTF-8'?>\n" +
            "<system>\n" +
            "    <pattern name=\"Factory Method\">\n" +
            "        <instance>\n" +
            "            <role name=\"Creator\" element=\"org.bonitasoft.engine.actor.mapping.model.SActorBuilderFactory\"/>\n" +
            "            <role name=\"FactoryMethod()\" element=\"org.bonitasoft.engine.actor.mapping.model.SActorBuilderFactory::create(java.lang.String, long, boolean):org.bonitasoft.engine.actor.mapping.model.SActorBuilder\"/>\n" +
            "            <role name=\"ConcreteCreator\" element=\"org.bonitasoft.engine.actor.mapping.model.impl.SActorBuilderFactoryImpl\"/>\n" +
            "            <role name=\"Product\" element=\"org.bonitasoft.engine.actor.mapping.model.SActorBuilder\"/>\n" +
            "        </instance>\n" +
            "        <instance>\n" +
            "            <role name=\"Creator\" element=\"org.bonitasoft.engine.api.PlatformLoginAPI\"/>\n" +
            "            <role name=\"FactoryMethod()\" element=\"org.bonitasoft.engine.api.PlatformLoginAPI::login(java.lang.String, java.lang.String):org.bonitasoft.engine.session.PlatformSession\"/>\n" +
            "            <role name=\"ConcreteCreator\" element=\"org.bonitasoft.engine.api.impl.PlatformLoginAPIImpl\"/>\n" +
            "            <role name=\"Product\" element=\"org.bonitasoft.engine.session.PlatformSession\"/>\n" +
            "        </instance>\n" +
            "    </pattern>\n" +
            "    <pattern name=\"Prototype\"> </pattern>\n" +
            "    <pattern name=\"Singleton\">\n" +
            "        <instance>\n" +
            "            <role name=\"Singleton\" element=\"org.bonitasoft.console.common.server.preferences.properties.PlatformTenantConfigProperties\"/>\n" +
            "            <role name=\"uniqueInstance\" element=\"private static org.bonitasoft.console.common.server.preferences.properties.PlatformTenantConfigProperties instance\"/>\n" +
            "        </instance>\n" +
            "        <instance>\n" +
            "            <role name=\"Singleton\" element=\"org.bonitasoft.console.common.server.sso.InternalSSOManager\"/>\n" +
            "            <role name=\"uniqueInstance\" element=\"private static org.bonitasoft.console.common.server.sso.InternalSSOManager instance\"/>\n" +
            "        </instance>\n" +
            "    </pattern>\n" +
            "    <pattern name=\"(Object)Adapter-Command\">\n" +
            "        <instance>\n" +
            "            <role name=\"Adaptee/Receiver\" element=\"org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClientCreator\"/>\n" +
            "            <role name=\"Adapter/ConcreteCommand\" element=\"org.bonitasoft.web.rest.server.api.organization.APICustomUserInfoDefinition\"/>\n" +
            "            <role name=\"Request()/Execute()\" element=\"org.bonitasoft.web.rest.server.api.organization.APICustomUserInfoDefinition::delete(java.util.List):void\"/>\n" +
            "        </instance>\n" +
            "        <instance>\n" +
            "            <role name=\"Adaptee/Receiver\" element=\"org.bonitasoft.engine.actor.xml.Actor\"/>\n" +
            "            <role name=\"Adapter/ConcreteCommand\" element=\"org.bonitasoft.engine.actor.xml.ActorBinding\"/>\n" +
            "            <role name=\"Request()/Execute()\" element=\"org.bonitasoft.engine.actor.xml.ActorBinding::setAttributes(java.util.Map):void\"/>\n" +
            "            <role name=\"Request()/Execute()\" element=\"org.bonitasoft.engine.actor.xml.ActorBinding::setChildObject(java.lang.String, java.lang.Object):void\"/>\n" +
            "        </instance>\n" +
            "    </pattern>\n" +
            "    <pattern name=\"Composite\"> </pattern>\n" +
            "    <pattern name=\"State-Strategy\">\n" +
            "        <instance>\n" +
            "            <role name=\"Context\" element=\"org.bonitasoft.web.rest.server.datastore.bpm.flownode.TaskFinder\"/>\n" +
            "            <role name=\"State/Strategy\" element=\"org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet\"/>\n" +
            "            <role name=\"ConcreteState/Strategy\" element=\"org.bonitasoft.web.rest.server.datastore.profile.GetBonitaPageHelper\"/>\n" +
            "            <role name=\"ConcreteState/Strategy\" element=\"org.bonitasoft.web.rest.server.datastore.ComposedDatastore\"/>\n" +
            "            <role name=\"ConcreteState/Strategy\" element=\"org.bonitasoft.web.rest.server.api.bpm.flownode.AbstractAPIFlowNode$1\"/>\n" +
            "            <role name=\"ConcreteState/Strategy\" element=\"org.bonitasoft.web.rest.server.datastore.organization.GroupDatastore\"/>\n" +
            "            <role name=\"ConcreteState/Strategy\" element=\"org.bonitasoft.web.rest.server.datastore.bpm.flownode.AbstractFlowNodeDatastore\"/>\n" +
            "            <role name=\"ConcreteState/Strategy\" element=\"org.bonitasoft.web.rest.server.datastore.bpm.cases.ArchivedCaseDatastore\"/>\n" +
            "            <role name=\"ConcreteState/Strategy\" element=\"org.bonitasoft.web.rest.server.datastore.organization.RoleDatastore\"/>\n" +
            "        </instance>\n" +
            "        <instance>\n" +
            "            <role name=\"Context\" element=\"org.bonitasoft.engine.execution.work.ExecuteConnectorWork$EvaluateConnectorOutputsTxContent\"/>\n" +
            "            <role name=\"State/Strategy\" element=\"org.bonitasoft.engine.execution.work.ExecuteConnectorWork\"/>\n" +
            "            <role name=\"ConcreteState/Strategy\" element=\"org.bonitasoft.engine.execution.work.ExecuteConnectorOfProcess\"/>\n" +
            "            <role name=\"ConcreteState/Strategy\" element=\"org.bonitasoft.engine.execution.work.ExecuteConnectorOfActivity\"/>\n" +
            "        </instance>\n" +
            "    </pattern>\n" +
            "</system>";

    @Test
    public void exportToXML() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLUtil.exportToXML(out, testSystem);
        Input.Builder control = Input.fromByteArray(testString.getBytes());
        Input.Builder test = Input.fromByteArray(out.toByteArray());
        Diff d = DiffBuilder.compare(control).withTest(test).ignoreWhitespace().build();

        assert !d.hasDifferences();
    }

    @Test
    public void importFromXML() throws Exception {
        ByteArrayOutputStream controlOut = new ByteArrayOutputStream();
        XMLUtil.exportToXML(controlOut, testSystem);

        ByteArrayInputStream testIn = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        XMLUtil.exportToXML(testOut, XMLUtil.importFromXML(testIn));

        Input.Builder control = Input.fromByteArray(controlOut.toByteArray());
        Input.Builder test = Input.fromByteArray(testOut.toByteArray());
        Diff d = DiffBuilder.compare(control).withTest(test).ignoreWhitespace().build();

        assert !d.hasDifferences();
    }

}