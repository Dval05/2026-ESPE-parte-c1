package es.upm.grise.profundizacion.file;

import java.util.ArrayList;
import java.util.List;

public class File {

    private FileType type;
    private List<Character> content;

	/*
	 * Constructor
	 */
    public File() {
        this.content = new ArrayList<>();
    }

	/*
	 * Method to code / test
	 */
    public void addProperty(char[] newcontent) throws InvalidContentException, WrongFileTypeException {
        // Verificar si newcontent es null
        if (newcontent == null) {
            throw new InvalidContentException("El contenido no puede ser null");
        }
        
        // Verificar si el tipo de archivo es IMAGE
        if (this.type == FileType.IMAGE) {
            throw new WrongFileTypeException("No se puede agregar propiedades a un archivo de tipo IMAGE");
        }
        
        // Añadir el newcontent al content existente
        for (char c : newcontent) {
            this.content.add(c);
        }
    }

	/*
	 * Method to code / test
	 */
    public long getCRC32() {
        // Si el content está vacío, devolver 0
        if (this.content.isEmpty()) {
            return 0L;
        }
        
        // Convertir ArrayList<Character> a byte[]
        byte[] bytes = new byte[this.content.size()];
        for (int i = 0; i < this.content.size(); i++) {
            // Obtener el byte menos significativo
            bytes[i] = (byte) (this.content.get(i) & 0x00FF);
        }
        
        // Calcular y devolver el CRC32
        FileUtils fileUtils = new FileUtils();
        return fileUtils.calculateCRC32(bytes);
    }
    
    
	/*
	 * Setters/getters
	 */
    public void setType(FileType type) {
    	
    	this.type = type;
    	
    }
    
    public List<Character> getContent() {
    	
    	return content;
    	
    }
    
}
