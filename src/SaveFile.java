import java.io.Serializable;


public class SaveFile implements Serializable{
    private static final long serialVersionUID = 1L;

	private String saveText;
	public static void main(String[] args) {
   
    }
	public SaveFile(String saveText) {
		super();
		this.saveText = saveText;
	}
	
	//public void setSaveText(String setText){
	//	saveText = setText;
	//}
	
	public String getSaveText(){
		return saveText;
	}
}
