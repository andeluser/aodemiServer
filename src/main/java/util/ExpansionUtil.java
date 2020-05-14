package util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ExpansionUtil {

	private Element List = null;

	public ExpansionUtil() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {

		InputStream fileStream = this.getClass().getResourceAsStream("expansion.xml");

		Document document= DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(fileStream);

		List = document.getDocumentElement();
	}

	/**
	 * 復帰有無判定。復帰持ちの対象だった場合はtrueを返却する
	 */
	public boolean returnCheack(String cardId) {

		boolean ret = false;
		NodeList nodeList = List.getElementsByTagName("return");

		for (int i = 0; i < nodeList.getLength(); i++) {
			// 要素をElementにキャストする
			Element book = (Element) nodeList.item(i);

			// 要素の属性値と、テキストノードの値を取得する
			String content = book.getTextContent();

			String[] itemList = content.split(",");

			for (int j = 0; j < itemList.length; j++) {
				if (cardId.equals(itemList[j])) {
					ret = true;
				}
			}
		}

		return ret;
	}

}
