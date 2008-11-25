package org.javarosa.core.model;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.instance.QuestionDataElement;


/**
 * An IAnswerDataSerializer returns an object that can be serialized
 * into some external storage format, IE: XForm, from an AnswerData
 * object. Each serializer is responsible for identifying what 
 * implementations of AnswerData it is able to serialize properly. 
 *  
 * Additionally, each serialzer is responsible for extending the types
 * that it can serialize by registering other serializers.   
 *    
 * @author Clayton Sims
 *
 */
public interface IAnswerDataSerializer {
	
	/**
	 * Identifies whether this serializer can turn the AnswerData 
	 * object inside of the given element into an external format.
	 *  
	 * @param element The element whose data is to be serialzed
	 * @return true if this can meaningfully serialze the provided
	 * object. false otherwise
	 */
	boolean canSerialize(QuestionDataElement element);
	
	/**
	 * Serializes the given data object into a format that can
	 * be stored externally using the defined schemas
	 * 
	 * @param element The element whose data is to be serialzed
	 * @param schema the schema containing the necessary bindings to determine
	 * the elements data type
	 * @return An implementation-specific representation of the
	 * given object if canSerialize() would return true for that
	 * object. False otherwise.
	 */
	Object serializeAnswerData(QuestionDataElement element, FormDef schema);
	
	/**
	 * Serializes the given data object into a format that can
	 * be stored externally.
	 * 
	 * @param data The element whose data is to be serialzed
	 * @return An implementation-specific representation of the
	 * given object if canSerialize() would return true for that
	 * object. False otherwise.
	 */
	Object serializeAnswerData(IAnswerData data);
	
	/**
	 * Extends the serializing capabilities of this serializer
	 * by registering another, and allowing this serializer
	 * to operate on all of the data types that the argument
	 * can.
	 * 
	 * @param ads An IAnswerDataSerializer
	 */
	void registerAnswerSerializer(IAnswerDataSerializer ads);
}