package com.studies.SpellingChecker;

import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;

public class LanguageToolInstance {
	private static LanguageToolInstance instance;
	private JLanguageTool langTool;
	public static LanguageToolInstance getInstance() {
		if(instance == null) {
			instance = new LanguageToolInstance();
			instance.setLangTool(new JLanguageTool(new AmericanEnglish()));
		}
		return instance;
			
	}
	public JLanguageTool getLangTool() {
		return langTool;
	}
	public void setLangTool(JLanguageTool langTool) {
		this.langTool = langTool;
	}
	
	
}
