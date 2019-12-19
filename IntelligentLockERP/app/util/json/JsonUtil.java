package util.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.Logger.ALogger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * @author xuml
 * @date 2016年5月27日下午3:10:02
 * @discription
 */
public class JsonUtil {
	private static final ObjectMapper objectMapper;
	private static ALogger logger = Logger.of(JsonUtil.class);
	static {
		objectMapper = new ObjectMapper();
		
		objectMapper.configure(
						DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
						false);// 遇到未知属性不处理
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,
				false);// 空对象不处理
		
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true); // 单引号处理
		
		objectMapper.getSerializerProvider().setNullValueSerializer(new NullSerializer());
		
	}
	
//	空字段不返回
//	public static void setNullValueSerializer(){
//		objectMapper.setSerializationInclusion(Include.NON_NULL);
//	}
	
	
	/**
	 * 对象转为json字符串
	 * @param object
	 * @return
	 */
	public static String parseObj(Object object) {
		try {
			return getObjectMapper().writeValueAsString(object);
		} catch (JsonParseException ex) {  
            logger.debug(ex.getMessage(), ex);  
        } catch (JsonMappingException ex) {  
            logger.debug(ex.getMessage(), ex);  
        } catch (IOException ex) {  
            logger.debug(ex.getMessage(), ex);  
        }  
		return null;
	}
	/**
	 * 对象转为字符串并写到writer里
	 * @param object
	 * @param writer
	 */
	public static void writeJsonString(Object object, Writer writer) {
		try {
			getObjectMapper().writeValue(writer, object);
		} catch (JsonGenerationException ex) {  
            logger.debug(ex.getMessage(), ex);  
        } catch (JsonMappingException ex) {  
            logger.debug(ex.getMessage(), ex);  
        } catch (IOException ex) {  
            logger.debug(ex.getMessage(), ex);  
        }  
	}
	/**
	 * json字符串转为map对象
	 * @param jsonString
	 * @return
	 */
	public static Map<?, ?> parseJson(String jsonString) {
		JsonNode jn = null;
		try {
			jn = getObjectMapper().readTree(jsonString);
		} catch (IOException ex) {
			logger.debug(ex.getMessage(),ex);
		}
		return (Map<?, ?>) JsonNodeToMap(jn);
	}
	/**
	 * json字符串不为list时转为map
	 * json字符串为list转为List<Map<String,Object>>
	 * @param jsonString
	 * @return
	 */
	public static Object parseJson2MapOrList(String jsonString) {
		JsonNode jn = null;
		try {
			jn = getObjectMapper().readTree(jsonString);
		} catch (IOException ex) {
			logger.debug(ex.getMessage(),ex);
		}
		return JsonNodeToMap(jn);
	}
	public static JsonNode parseJson2JsonNode(String jsonString) {
		JsonNode jn = null;
		try {
			jn = getObjectMapper().readTree(jsonString);
		} catch (IOException ex) {
			logger.debug(ex.getMessage(),ex);
		}
		return jn;
	}
	/**
	 * json字符串转为classType类型对象
	 * @param jsonString
	 * @param classType
	 * @return
	 */
	public static <T> T parseJson(String jsonString, Class<T> classType) {
		try {
			return getObjectMapper().readValue(jsonString, classType);
		} catch (Exception ex) {
			logger.debug(ex.getMessage(),ex);
		}
		return null;
	}
	/**
	 * json字符串转换为复杂对象  依赖于typeReference
	 * eg.new TypeReference<List<转换类型>>(){}
	 * @param jsonString
	 * @param typeReference
	 * @return
	 */
	public static <T> T parseJson(String jsonString,
			TypeReference<T> typeReference) {
		try {
			return getObjectMapper().readValue(jsonString, typeReference);
		} catch (Exception ex) {
			logger.debug(ex.getMessage(),ex);
		}
		return null;
	}
	/**
	 * json字符串转为List<classType>
	 * @param jsonString
	 * @param classType
	 * @return
	 */
	public static <T> List<T> parseJson2List(String jsonString,Class<T> classType){
		try{
			JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(List.class, classType);
			return getObjectMapper().readValue(jsonString, javaType);
		}catch (Exception ex) {
			logger.debug(ex.getMessage(),ex);
		}
		return null;
	}
	/**
	 * json字符串转为object
	 * 不需要强转
	 * @param jsonString
	 * @return
	 */
	public static <T> T parseJsonT(String jsonString) {
		try {
			return getObjectMapper().readValue(jsonString,
					new TypeReference<Object>() {
					});
		} catch (Exception ex) {
			logger.debug(ex.getMessage(),ex);
		}
		return null;
	}
	/**
	 * 实体转为map
	 * @param bean
	 * @return
	 */
	public static <T> Map<?, ?> bean2Map(Object bean) {
		try {
			return (Map<?, ?>) getObjectMapper().convertValue(bean, Map.class);
		} catch (Exception ex) {
			logger.debug(ex.getMessage(),ex);
		}
		return null;
	}
	/**
	 * map转为实体
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static <T> T map2Bean(Map<?, ?> map, Class<T> clazz) {
		try {
			return getObjectMapper().convertValue(map, clazz);
		} catch (Exception ex) {
			logger.debug(ex.getMessage(),ex);
		}
		return null;
	}

	public static Object JsonNodeToMap(JsonNode root) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (root == null) {
			return map;
		}
		if (root.isArray()) {
			List<Object> list = new ArrayList<Object>();
			for (JsonNode node : root) {
				Object nmp = JsonNodeToMap(node);
				list.add(nmp);
			}
			return list;
		}
		if (root.isTextual()) {
			try {
				return ((TextNode) root).asText();
			} catch (Exception e) {
				return root.toString();
			}
		}
		Iterator<String> iter = root.fieldNames();
		while (iter.hasNext()) {
			String key = iter.next();
			JsonNode ele = root.get(key);
			if (ele.isObject())
				map.put(key, JsonNodeToMap(ele));
			else if (ele.isTextual())
				map.put(key, ((TextNode) ele).textValue());
			else if (ele.isArray())
				map.put(key, JsonNodeToMap(ele));
			else {
				map.put(key, ele.toString());
			}
		}
		return map;
	}
	
	public static ObjectMapper getObjectMapper(){
		return objectMapper;
	}
	
}
