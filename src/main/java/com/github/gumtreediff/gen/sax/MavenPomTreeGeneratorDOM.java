package com.github.gumtreediff.gen.sax;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.xml.sax.SAXException;

import com.github.gumtreediff.gen.Register;
import com.github.gumtreediff.gen.TreeGenerator;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

@Register(id = "xml-sax", accept = {"\\.xml$", "\\.xsd$", "\\.wadl$"})
public class MavenPomTreeGeneratorDOM extends TreeGenerator {
	public static final String DOCUMENT = "Document";
	public static final String ATTR = "Attr";
	public static final String CDATA = "CData";
	public static final String ELT = "Elt";
	public static final String VALUE = "Value";

	public static final int CDATA_ID = 3;
	public static final int DOCUMENT_ID = 0;
	public static final int ATTR_ID = 2;
	public static final int ELT_ID = 1;
	public static final int VALUE_ID = 4;

	TreeContext tc = new TreeContext();

    public TreeContext generate(Reader reader) throws IOException {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = (Document) builder.build(reader);
//            org.w3c.dom.Document document = PositionalXMLReader.readXML(reader);
//
//            DOMBuilder builder = new DOMBuilder();
//            Document doc = builder.build(document);

            Element rootElement = doc.getRootElement();
            //		System.out.println("<" + rootElement.getName()+">");

            //		List childNodes = rootElement.getChildren();
            ITree t = tc.createTree(ELT_ID, rootElement.getName(), ELT);

            tc.setRoot(t);
            printNodes(rootElement,"  ",t);

            //		System.out.println("</" + rootElement.getName()+">");

            return tc;
        } catch(Exception e) {
            throw new IOException(e);
        }
    }

	private void printNodes(Element documentElement, String indentation, ITree parent) {
		List childNodes = documentElement.getChildren();
		for (Object object : childNodes) {
			Element e = (Element)object;
			ITree t = tc.createTree(ELT_ID, e.getName(), ELT);

			parent.addChild(t);
			if(e.getChildren()!=null && e.getChildren().size()>0) {
//				System.out.println(indentation+ "<"+e.getName()+">");
				printNodes(e, indentation+"  ",t);
//				System.out.println(indentation+"</"+e.getName()+">");
			} else {
//				System.out.println(indentation+ "<"+e.getName()+">"+e.getTextTrim()+"</"+e.getName()+">");
				ITree textTree = tc.createTree(CDATA_ID, e.getTextTrim(), CDATA);
				t.addChild(textTree);
			}
		}
	}

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, JDOMException {
//		new MavenPomTreeGeneratorDOM().generate(new File("/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/manualEvaluation/activemq/2_0_old[87b654a533fe09b91c6f6390c0a46089128fded3]_pom.xml"));
	}
}
