package demo.work;


import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JaxbUtil {

	/**
	 *
	 * @param obj
	 * @return
	 */
	public static String convertToXml(Object obj) {
		return convertToXml(obj, "GB2312");
	}

	/**
	 *
	 * @param obj
	 * @param encoding
	 * @return
	 */
	public static String convertToXml(Object obj, String encoding) {
		String result = null;
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

			StringWriter writer = new StringWriter();
			marshaller.marshal(obj, writer);
			result = writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * @param xml
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertToJavaBean(String xml, Class<T> c) {
		T t = null;
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(new StringReader(xml));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	public static Map<String, Object> xmlStrToMap(String xmlStr) throws Exception {
		if(StringUtils.isEmpty(xmlStr)) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Document doc = DocumentHelper.parseText(xmlStr);
		Element root = doc.getRootElement();
		List children = root.elements();
		if(children != null && children.size() > 0) {
			for(int i = 0; i < children.size(); i++) {
				Element child = (Element)children.get(i);
				if("body".equals(child.getName())){
					map.put(child.getName(), getElementData(child.elements()));
				}else{
					map.put(child.getName(), child.getTextTrim());
				}
			}
		}
		return map;
	}
	public static Map<String, Object> getElementData(List children){
		Map<String, Object> map = new HashMap<String, Object>();
		if(children != null && children.size() > 0) {
			for(int i = 0; i < children.size(); i++) {
				Element child = (Element)children.get(i);
				map.put(child.getName(), child.getTextTrim());
			}
		}
		return map;
	}

	private static Object convertValType(Object value, Class fieldTypeClass) {
		Object retVal = null;
		if(Long.class.getName().equals(fieldTypeClass.getName())
				|| long.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Long.parseLong(value.toString());
		} else if(Integer.class.getName().equals(fieldTypeClass.getName())
				|| int.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Integer.parseInt(value.toString());
		} else if(Float.class.getName().equals(fieldTypeClass.getName())
				|| float.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Float.parseFloat(value.toString());
		} else if(Double.class.getName().equals(fieldTypeClass.getName())
				|| double.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Double.parseDouble(value.toString());
		} else {
			retVal = value;
		}
		return retVal;
	}


	private static Field getClassField(Class clazz, String fieldName) {
		if( Object.class.getName().equals(clazz.getName())) {
			return null;
		}
		Field []declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}

		Class superClass = clazz.getSuperclass();
		if(superClass != null) {
			return getClassField(superClass, fieldName);
		}
		return null;
	}
	public static Map<String, String> xml2Map(String message) {
		Map<String, String> data = new LinkedHashMap<String, String>();
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(message);
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}

		Element root = doc.getRootElement();
		String path = "/" + root.getName();
		element2Map(root, data, path);
		return data;
	}
	private static void element2Map(Element ele, Map<String, String> data, String path) {
		if(ele == null) {
			return;
		}
		List<Element> childrens = ele.elements();
		if(childrens != null && childrens.size() > 0) {
			Element pre = null;
			Element cur = null;
			Element next = null;
			int nodeIndex = 1;
			int length = childrens.size();
			for (int i = 0; i < length; i++) {
				cur = childrens.get(i);
				String nodePath = path + "/" + cur.getName();
				if(pre == null) {
					next = childrens.get(i + 1);
					if(next.getName().equals(cur.getName())) {
						nodePath += "[" + nodeIndex + "]";
						nodeIndex++;
					}
				}
				else {
					if(pre.getName().equals(cur.getName())) {
						nodePath += "[" + nodeIndex + "]";
						nodeIndex++;
					}
					else {
						nodeIndex = 1;
					}
				}
				element2Map(cur, data, nodePath);
				pre = cur;
			}
		}
		else {

			data.put(path, ele.getText());

		}

	}
}