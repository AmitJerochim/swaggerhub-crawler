/**
 * customized Exception to which is thrown by OASFileCollector.verifyCorrectness if openapi File is invalid
 */
public class OASDefintionInvalidException extends Exception{
    public OASDefintionInvalidException(String message){
        super(message);
    }
}
