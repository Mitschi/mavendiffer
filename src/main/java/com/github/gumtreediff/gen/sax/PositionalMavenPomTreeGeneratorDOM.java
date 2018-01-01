package com.github.gumtreediff.gen.sax;

import com.github.gumtreediff.gen.Register;
import com.github.gumtreediff.gen.TreeGenerator;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.located.LocatedElement;
import org.jdom2.located.LocatedJDOMFactory;
import org.jdom2.located.LocatedText;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Register(id = "xml-sax", accept = {"\\.xml$", "\\.xsd$", "\\.wadl$"})
public class PositionalMavenPomTreeGeneratorDOM extends TreeGenerator {
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
            builder.setJDOMFactory(new LocatedJDOMFactory());
            Document doc = (Document) builder.build(reader);
//            org.w3c.dom.Document document = PositionalXMLReader.readXML(reader);
//
//            DOMBuilder builder = new DOMBuilder();
//            Document doc = builder.build(document);

            LocatedElement rootElement =(LocatedElement) doc.getRootElement();
            //		System.out.println("<" + rootElement.getName()+">");

            //		List childNodes = rootElement.getChildren();
            ITree t = tc.createTree(ELT_ID, rootElement.getName(), ELT);
            t.setPos(rootElement.getLine());
            t.setLength(rootElement.getColumn());

            tc.setRoot(t);
            printNodes(rootElement,"  ",t);

            //		System.out.println("</" + rootElement.getName()+">");

            return tc;
        } catch(Exception e) {
            throw new IOException(e);
        }
    }

    private void printNodes(LocatedElement documentElement, String indentation, ITree parent) {
        List childNodes = documentElement.getChildren();
        for (Object object : childNodes) {
            LocatedElement e = (LocatedElement)object;
            ITree t = tc.createTree(ELT_ID, e.getName(), ELT);
//            t.setPos(e.getLine()-e.getName().length()-2);
//            t.setLength(e.getColumn());

            parent.addChild(t);

            int startLine=e.getLine();
            int startOffset=e.getColumn()-e.getName().length()-2-1; //-2 for <>, and -1 for 0 index

            int numberOfLines=getNumberOfLines(e,0);

            int endLine=startLine+numberOfLines;
            String endOffsetString=getEndOffset(e,"");
            int endOffset = endOffsetString.substring(endOffsetString.lastIndexOf("\n")+1).length();

            //we need startLine/startOffset
            //and endLine/endOffset
            t.setPos(startLine*10000+endLine);
            t.setLength(startOffset*10000+endOffset);

            if(e.getChildren()!=null && e.getChildren().size()>0) {

//				System.out.println(indentation+ "<"+e.getName()+">");
                printNodes(e, indentation+"  ",t);
//				System.out.println(indentation+"</"+e.getName()+">");
            } else {
//				System.out.println(indentation+ "<"+e.getName()+">"+e.getTextTrim()+"</"+e.getName()+">"+" ("+e.getLine()+" // "+e.getColumn()+")");
                ITree textTree = tc.createTree(CDATA_ID, e.getTextTrim(), CDATA);
//                textTree.setPos(e.getLine());
//                textTree.setLength(e.getColumn());
                textTree.setPos(e.getLine()*10000+e.getLine());
                textTree.setLength(e.getColumn()*10000+(e.getColumn()+e.getTextTrim().length()));
                t.addChild(textTree);
            }
        }
    }

    private String getEndOffset(Content e, String currentOffset) {
        if(e instanceof LocatedText) {
            String text = ((LocatedText) e).getText();
//            int startIndex = text.lastIndexOf('\n')==-1?text.length()-1:text.lastIndexOf('\n');
//            if(StringUtils.countMatches(text,"\n")>0) {
//                return text.substring(startIndex+1);
//            } else {
//                return text.substring(startIndex);
//            }
          return text;
        } else {
            if(e instanceof LocatedElement) {
                String sum = "<"+((LocatedElement) e).getName()+">";

                for (Content content : ((LocatedElement) e).getContent()) {
                    sum += getEndOffset(content, currentOffset);
                }
                sum += "</"+((LocatedElement) e).getName()+">";
                return sum;
            }
            return "";
        }
    }

    private int getNumberOfLines(Content e, int currentLength) {
        if(e instanceof LocatedText) {
            return StringUtils.countMatches(((LocatedText) e).getText(),"\n");
        } else {
            if(e instanceof LocatedElement) {
                int sum = 0;
                for (Content content : ((LocatedElement) e).getContent()) {
                    sum += getNumberOfLines(content, currentLength);
                }
                return sum;
            }
            return 0;
        }
    }

    private int getLength(Content e, int currentLength) {
        if(e instanceof LocatedText) {
            return currentLength+((LocatedText)e).getText().length();
        } else {
            if(e instanceof LocatedElement) {
                int sum = 2*(2+((LocatedElement) e).getName().length())+1;
                for (Content content : ((LocatedElement) e).getContent()) {
                    sum += getLength(content, currentLength);
                }
                return sum;
            }
            return 0;
        }
    }
}
