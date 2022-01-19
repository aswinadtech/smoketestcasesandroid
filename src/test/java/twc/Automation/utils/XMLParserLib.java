package twc.Automation.utils;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XMLParserLib {
	public static void appendNewTextNode(Document doc, Element e, String elementName, String value) {
		Node node = null;
		if (!value.isEmpty()) {
			node = doc.createElement(elementName);
			node.appendChild(doc.createTextNode(value));
			e.appendChild(node);
		}
	}

	public static Node findFirstChildNode(String name, Node root) {
		List<Node> list = findChildNodes(name, root);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public static List<Node> findChildNodes(String name, Node root) {
		List<Node> nodes = new ArrayList<Node>();

		if (root != null && root.hasChildNodes() && root.getNodeType() == Node.ELEMENT_NODE) {
			NodeList children = root.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child.getNodeName().equals(name)) {
					nodes.add(child);
				}
			}
		}
		return nodes;
	}

	/* Useful but only for debugging
	public static void printNote(NodeList nodeList) {
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				// get node name and value
				System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
				System.out.println("Node Value =" + tempNode.getTextContent());

				if (tempNode.hasAttributes()) {
					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();

					for (int i = 0; i < nodeMap.getLength(); i++) {
						Node node = nodeMap.item(i);
						System.out.println("attr name : " + node.getNodeName());
						System.out.println("attr value : " + node.getNodeValue());
					}
				}

				if (tempNode.hasChildNodes()) {
					// loop again if has child nodes
					printNote(tempNode.getChildNodes());
				}
				System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
			}
		}
	}
	*/
}
