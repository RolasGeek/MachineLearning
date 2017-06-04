package com.studies.classifiers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.encog.neural.networks.BasicNetwork;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.SpellingChecker.LanguageToolInstance;
import com.studies.model.Messages;
import com.studies.service.MessageService;

public class prepareClassifiers {

	private static String spellCheckFileDir = "C:\\Temp";
	private static String spellCheckFileName = "spellChecking.txt";
	public static String spellCheckFile = spellCheckFileDir + "//" + spellCheckFileName;
	private static BasicNetwork basicNetwork;
	
	public static Classifier prepareSpellingCheck() {
		Classifier classifier = new Classifier();
		HashMap<String, Object> data = countSpellingMistakes();
		classifier.setValues(data);
		return classifier;
	}
	
	private static HashMap<String, Object> countSpellingMistakes() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		List<Messages> all = MessageService.getInstance().listAll(0, 10000);
		//JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());
		DataClass users = new DataClass();
		List<RuleMatch> matches;
		String words[], text = "", name = "";
		File fileDir = new File(spellCheckFileDir);
		if (!fileDir.exists()){
			fileDir.mkdir();
		}
		File f = new File(spellCheckFile);
		f.delete();
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double koef = 0.0, prev = 0.0;
		//System.out.println("old koef:"+users.getTypes().get(users.getByName("Jonas")).getKoef());
		for (Messages message : all) {
			text = message.getMessage();
			name = message.getName();
			try {
				matches = LanguageToolInstance.getInstance().getLangTool().check(text);
				words = text.split("[^a-zA-Z0-9\'“”’\"$]");
				koef = (matches.size()/(double)words.length);
				//System.out.println("matches: " + matches.size());
				//System.out.println("koef: " + koef);
				Integer index = users.getByName(name);
				User u = users.getTypes().get(index);
				String s = u.getName() +" "+ koef + "\n";
				writeFile(f, s);

				u.setAmount(u.getAmount()+1);
				u.setKoef(u.getKoef()+koef);
				users.getTypes().set(index, u);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			text="";name="";koef=0.0;prev=0.0;
		}
		for(User u : users.getTypes()) {
			Integer index = users.getByName(u.getName());
			u.setKoef(u.getKoef()/u.getAmount());
			users.getTypes().set(index, u);
			data.put(u.getName(), u.getKoef());
		}
		//System.out.println("new koef:"+users.getTypes().get(users.getByName("Jonas")).getKoef());
		createNeuron(f);
		
		return data;
	}
	
	public static String getUserFromDouble(double d) {
		File f = new File(spellCheckFile);
		ArrayList<String> userList = new ArrayList<String>();
		int val = (int) Math.round(d);
		int i = 0;
		Scanner sc;
		try {
			sc = new Scanner(f);
			sc.useLocale(Locale.US);
			while (sc.hasNext()){
                String s = sc.next();
                double _ = sc.nextDouble();
                if (!userList.contains(s)) {
                	if(i==val) {
                		sc.close();
                		return s;
                	}
                	userList.add(s);
                	i++;
                }
            }
            sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "null";
		}
        
		return "null";
	}

	private static void createNeuron(File f){
		Neuron neuron = new Neuron();
		basicNetwork = neuron.createNeuron(f);
	}

	public static BasicNetwork getNetwork() {
		return basicNetwork;
	}
	
	private static void writeFile(File file, String text) throws IOException {
		BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
		output.write(text);
		output.close();
	}

	private static HashMap<String, Object> countWords() {
		HashMap<String, Object> data = new HashMap<>();
		List<Messages> all = MessageService.getInstance().listAll(0, 10000);
		for (Messages messages : all) {
			String words[] = messages.getMessage().split("[^a-zA-Z0-9\'“”’\"$]");
			for (String string : words) {
				if(string != "") {
				DataClass exist = (DataClass) data.get(string);
				if(exist != null) {
					Integer index = exist.getByName(messages.getName());
					User u = exist.getTypes().get(index);
					u.setAmount(u.getAmount()+1);
					exist.getTypes().set(index, u);
					data.replace(string, exist);
				} else {
					DataClass empty = new DataClass();
					Integer index = empty.getByName(messages.getName());
					empty.getTypes().set(index, new User(messages.getName(), 0.0, 1));
					data.put(string, empty);
				}
				}
			}
		}
		return data;
	}
	
	public static Classifier prepareWordCount() {
		Classifier classifier = new Classifier();
		HashMap<String, Object> data = countWords();
		for (String key : data.keySet()) {
			DataClass value = (DataClass) data.get(key);
			List<User> list = value.getTypes();
			for (User user : list) {
				if(user.getAmount() > 0) {
				Integer index = list.indexOf(user);
				Double d = (double)user.getAmount() / differetWords(user.getName(), data);
				user.setKoef(d);
				list.set(index, user);
				}
			}
			
		}
		classifier.setValues(data);
		return classifier;
	}
	
	
	private static Integer differetWords(String name, HashMap<String, Object> data) {
		Integer count = 0;
		for (String key : data.keySet()) {
			DataClass value = (DataClass) data.get(key);
			User u = value.getTypes().get(value.getByName(name));
			if(u.getAmount() > 0) {
				count++;
			}
		}
		return count;
	}
	

	public static Classifier prepareMessageLenght() {
		Classifier classifier = new Classifier();
		HashMap<String, Object> data = countLenghts();
		for (String key : data.keySet()) {
			DataClass value = (DataClass) data.get(key);
			List<User> list = value.getTypes();
			for (User user : list) {
				if(user.getAmount() > 0) {
				Integer index = list.indexOf(user);
				Double d = (double)user.getAmount() / MessageService.getInstance().countByName(user.getName());
				user.setKoef(d);
				list.set(index, user);
				}
			}
			
		}
		for (String key : data.keySet()) {
			DataClass obj = (DataClass) data.get(key);
			Collections.sort(obj.getTypes(), (o1, o2) -> Double.compare(o2.getKoef(), o1.getKoef()));
		}
		classifier.setValues(data);
		return classifier;
	}
	
	public static HashMap<String, Object> countLenghts() {
		HashMap<String, Object> data = new HashMap<>();
		List<Messages> all = MessageService.getInstance().listAll(0, 10000);
		for (Messages messages : all) {
			int l = messages.getMessage().length() / 10;
			String key  = Integer.toString(l);
			DataClass obj = (DataClass) data.get(key);
			if(obj != null) {
				Integer index = obj.getByName(messages.getName());
				User u = obj.getTypes().get(index);
				u.setAmount(u.getAmount()+1);
				obj.getTypes().set(index, u);
			} else {
				DataClass temp = new DataClass();
				Integer index = temp.getByName(messages.getName());
				temp.getTypes().set(index, new User(messages.getName(), 0.0, 1));
				data.put(key, temp);
			}
		}
		return data;
	}

	public String getSpellCheckFile(){
		return spellCheckFile;
	}
	

}
