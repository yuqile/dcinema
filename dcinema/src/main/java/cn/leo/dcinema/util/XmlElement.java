package cn.leo.dcinema.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class XmlElement {

	private String name;

	private Map<String, Object> attributes;

	private List<String> text;

	private Map<String, List<XmlElement>> children;

	private List<XmlElement> childrenList;

	private List<Object> all;

	private static final String TAG_LEFT = "<";

	private static final String TAG_RIGHT = ">";

	private static final String TAG_END = "/";

	private static final String EQUEL = "=";

	private static final String QUOT = "\"";

	private static final String SPACE = " ";

	private static final String[] REPLACE = { "&", "&amp;", "\"", "&quot;",
			"'", "&apos;", "<", "&lt;", ">", "&gt;" };

	private static final int REPLACE_LENGTH = REPLACE.length % 2 == 0 ? REPLACE.length
			: REPLACE.length - 1;

	private static final String TAG = "XmlElement";

	public XmlElement(String name) {
		this.name = name;
		attributes = new LinkedHashMap<String, Object>();
		text = new ArrayList<String>(10);
		children = new LinkedHashMap<String, List<XmlElement>>();
		childrenList = new ArrayList<XmlElement>(30);
		all = new ArrayList<Object>(100);
	}

	public String getName() {
		return name;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public String getAttribute(String name) {
		return (String) attributes.get(name);
	}

	public XmlElement addAttributes(Map<String, String> attributes) {
		if (attributes != null) {
			this.attributes.putAll(attributes);
		}
		return this;
	}

	public XmlElement setAttribute(String name, String value) {
		this.attributes.put(name, value);
		return this;
	}

	public List<String> getAllText() {
		return text;
	}

	public String getText(int index) {
		return text.size() > index ? text.get(index) : "";
	}

	public String getText() {
		return getText(0);
	}

	public XmlElement addText(String text) {
		this.text.add(text);
		this.all.add(text);
		return this;
	}

	public List<XmlElement> getAllChildren() {
		return childrenList;
	}

	public List<XmlElement> getChildren(String tagName) {
		return children.get(tagName);
	}

	public XmlElement getChild(String tagName, int index) {
		List<XmlElement> children = getChildren(tagName);
		return children != null && children.size() > index ? children
				.get(index) : null;
	}

	public XmlElement getChild(int index) {
		return childrenList.size() > index ? childrenList.get(index) : null;
	}

	public XmlElement addChild(XmlElement child) {
		if (child != null && child.name != null) {
			childrenList.add(child);
			all.add(child);
			if (!children.containsKey(child.name)) {
				children.put(child.name, new ArrayList<XmlElement>(10));
			}
			children.get(child.name).add(child);
		}
		return this;
	}

	public XmlElement addChildren(List<XmlElement> children) {
		if (children != null) {
			for (XmlElement element : children) {
				addChild(element);
			}
		}
		return this;
	}

	public List<Object> getAllTextAndChildren() {
		return all;
	}

	public XmlElement clear() {
		attributes.clear();
		text.clear();
		children.clear();
		childrenList.clear();
		all.clear();
		return this;
	}

	public static XmlElement parseXml(InputStream in)
			throws XmlPullParserException, IOException {
		if (in == null) {
			return null;
		}

		XmlElement result = null;
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(in, HTTP.UTF_8);
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				result = new XmlElement(xpp.getName());
				result.parseXml(xpp);
			} else {
				try {
					if (eventType != XmlPullParser.START_DOCUMENT) {
						Log.e(TAG, XmlPullParser.TYPES[eventType]);
					}
				} catch (Throwable t) {
					Log.e(TAG, "Oh! My God!" + t);
				}
			}
			eventType = xpp.next();
		}

		return result;
	}

	public void writeAsXml(Writer writer) throws IOException {
		addXml(writer, this);
	}

	public String toString() {
		StringWriter sw = new StringWriter();
		try {
			writeAsXml(sw);
		} catch (IOException e) {
			Log.e(TAG,"Oh! My God!"+ e.toString());
		}
		return sw.toString();
	}

	private void parseXml(XmlPullParser xpp) throws XmlPullParserException,
			IOException {
		for (int i = 0; i < xpp.getAttributeCount(); i++) {
			setAttribute(xpp.getAttributeName(i), xpp.getAttributeValue(i));
		}
		int eventType = xpp.next();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				XmlElement child = new XmlElement(xpp.getName());
				addChild(child);
				child.parseXml(xpp);
			}
			if (eventType == XmlPullParser.END_TAG) {
				return;
			}
			if (eventType == XmlPullParser.TEXT) {
				addText(xpp.getText());
			}
			eventType = xpp.next();
		}
	}

	private void addXml(Writer sb, XmlElement element) throws IOException {
		if (element.all.size() == 0) {
			element.addTag(sb);
		} else {
			element.addTagStart(sb);
			for (Object item : element.all) {
				if (item != null) {
					if (item instanceof String) {
						element.addText(sb, (String) item);
					} else {
						element.addXml(sb, (XmlElement) item);
					}
				}
			}
			element.addTagEnd(sb);
		}
	}

	private void addTagStart(Writer sb) throws IOException {
		sb.append(TAG_LEFT).append(name);
		addAttributes(sb);
		sb.append(TAG_RIGHT);
	}

	private void addTagEnd(Writer sb) throws IOException {
		sb.append(TAG_LEFT).append(TAG_END).append(name).append(TAG_RIGHT);
	}

	private void addTag(Writer sb) throws IOException {
		sb.append(TAG_LEFT).append(name);
		addAttributes(sb);
		sb.append(SPACE).append(TAG_END).append(TAG_RIGHT);
	}

	private void addAttributes(Writer sb) throws IOException {
		if (attributes != null) {
			for (Map.Entry<String, Object> item : attributes.entrySet()) {
				sb.append(SPACE).append(item.getKey()).append(EQUEL)
						.append(QUOT);
				addText(sb, (String) item.getValue());
				sb.append(QUOT);
			}
		}
	}

	private void addText(Writer sb, String text) throws IOException {
		if (text != null) {
			for (int i = 0; i < REPLACE_LENGTH; i += 2) {
				text = text.replace(REPLACE[i], REPLACE[i + 1]);
			}
			sb.append(text);
		}
	}
}
