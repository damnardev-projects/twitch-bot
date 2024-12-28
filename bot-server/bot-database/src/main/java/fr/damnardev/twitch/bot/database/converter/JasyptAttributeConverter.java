package fr.damnardev.twitch.bot.database.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jasypt.util.text.BasicTextEncryptor;

import org.springframework.beans.factory.annotation.Value;

@Converter
public class JasyptAttributeConverter implements AttributeConverter<String, String> {

	private final BasicTextEncryptor textEncryptor;

	public JasyptAttributeConverter(@Value("${jasypt.encryptor.password}") String password) {
		this.textEncryptor = new BasicTextEncryptor();
		this.textEncryptor.setPassword(password);
	}

	@Override
	public String convertToDatabaseColumn(String value) {
		if (value == null) {
			return null;
		}
		return this.textEncryptor.encrypt(value);
	}

	@Override
	public String convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return this.textEncryptor.decrypt(value);
	}

}
