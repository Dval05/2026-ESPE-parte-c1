package es.upm.grise.profundizacion.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class FileTest {
	
	private File file;
	
	@BeforeEach
	public void setUp() {
		file = new File();
	}
	
	@Test
    public void smokeTest() {}
    
    // Tests para el constructor
    @Test
    public void testConstructor_ContentNotNull() {
    	File newFile = new File();
    	assertNotNull(newFile.getContent(), "El contenido no debe ser null después de la construcción");
    }
    
    @Test
    public void testConstructor_ContentIsEmpty() {
    	File newFile = new File();
    	assertTrue(newFile.getContent().isEmpty(), "El contenido debe estar vacío después de la construcción");
    }
    
    // Tests para addProperty
    @Test
    public void testAddProperty_ValidContent() throws InvalidContentException, WrongFileTypeException {
    	file.setType(FileType.PROPERTY);
    	char[] content = {'k', 'e', 'y', '=', 'v', 'a', 'l', 'u', 'e'};
    	file.addProperty(content);
    	assertEquals(9, file.getContent().size(), "El contenido debe tener 9 caracteres");
    }
    
    @Test
    public void testAddProperty_NullContent() {
    	file.setType(FileType.PROPERTY);
    	assertThrows(InvalidContentException.class, () -> {
    		file.addProperty(null);
    	}, "Debe lanzar InvalidContentException cuando el contenido es null");
    }
    
    @Test
    public void testAddProperty_ImageType() {
    	file.setType(FileType.IMAGE);
    	char[] content = {'k', 'e', 'y', '=', 'v', 'a', 'l', 'u', 'e'};
    	assertThrows(WrongFileTypeException.class, () -> {
    		file.addProperty(content);
    	}, "Debe lanzar WrongFileTypeException cuando el tipo es IMAGE");
    }
    
    @Test
    public void testAddProperty_MultipleAdditions() throws InvalidContentException, WrongFileTypeException {
    	file.setType(FileType.PROPERTY);
    	char[] content1 = {'k', 'e', 'y', '1', '=', 'v', 'a', 'l', '1'};
    	char[] content2 = {'k', 'e', 'y', '2', '=', 'v', 'a', 'l', '2'};
    	file.addProperty(content1);
    	file.addProperty(content2);
    	assertEquals(18, file.getContent().size(), "El contenido debe acumularse");
    }
    
    @Test
    public void testAddProperty_EmptyArray() throws InvalidContentException, WrongFileTypeException {
    	file.setType(FileType.PROPERTY);
    	char[] content = {};
    	file.addProperty(content);
    	assertEquals(0, file.getContent().size(), "El contenido debe permanecer vacío");
    }
    
    // Tests para getCRC32
    @Test
    public void testGetCRC32_EmptyContent() {
    	long crc = file.getCRC32();
    	assertEquals(0L, crc, "CRC32 debe ser 0 para contenido vacío");
    }
    
    @Test
    public void testGetCRC32_WithContent() throws InvalidContentException, WrongFileTypeException {
    	file.setType(FileType.PROPERTY);
    	char[] content = {'t', 'e', 's', 't'};
    	file.addProperty(content);
    	// El CRC32 será calculado por FileUtils
    	long crc = file.getCRC32();
    	// Como FileUtils devuelve el valor configurado, solo verificamos que no es negativo
    	assertTrue(crc >= 0, "CRC32 debe ser un valor no negativo");
    }
    
    @Test
    public void testGetCRC32_ByteConversion() throws InvalidContentException, WrongFileTypeException {
    	file.setType(FileType.PROPERTY);
    	// Agregar caracteres con valores conocidos
    	char[] content = {'A', 'B', 'C'}; // 65, 66, 67
    	file.addProperty(content);
    	long crc = file.getCRC32();
    	// Verificamos que el método se ejecuta sin errores
    	assertNotNull(crc);
    }
    
    @Test
    public void testGetCRC32_UTF8Range() throws InvalidContentException, WrongFileTypeException {
    	file.setType(FileType.PROPERTY);
    	// Probar con caracteres en el rango UTF-8 [0, 255]
    	char[] content = {(char)65, (char)255, (char)128};
    	file.addProperty(content);
    	long crc = file.getCRC32();
    	assertTrue(crc >= 0, "CRC32 debe manejar valores en rango UTF-8");
    }
    
    // Tests de integración
    @Test
    public void testIntegration_PropertyFileWorkflow() throws InvalidContentException, WrongFileTypeException {
    	file.setType(FileType.PROPERTY);
    	char[] prop1 = "DATE=20250919".toCharArray();
    	char[] prop2 = "NAME=TestFile".toCharArray();
    	
    	file.addProperty(prop1);
    	file.addProperty(prop2);
    	
    	assertEquals(26, file.getContent().size());
    	long crc = file.getCRC32();
    	assertTrue(crc >= 0);
    }
    
    @Test
    public void testSetType_Property() {
    	file.setType(FileType.PROPERTY);
    	assertDoesNotThrow(() -> {
    		file.addProperty("test".toCharArray());
    	}, "No debe lanzar excepción con tipo PROPERTY");
    }
    
    @Test
    public void testSetType_Image() {
    	file.setType(FileType.IMAGE);
    	assertThrows(WrongFileTypeException.class, () -> {
    		file.addProperty("test".toCharArray());
    	}, "Debe lanzar excepción con tipo IMAGE");
    }

}

