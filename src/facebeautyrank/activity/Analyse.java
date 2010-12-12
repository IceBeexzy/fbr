package facebeautyrank.activity;

public class Analyse {
	public final double FACE_GOLDEN_RATIO=0.618;
	public final double FACE_HEIGHT_RATIO=0.333333;
	public final double EYES_WIGTH_RATIO = 0.42;
	public final double FACE_WIDTH_RATIO = 0.618;
	public final double CHIN_ANGLE_TAN = 1.6;
	
	private double[] measure=new double[7];
	private double distanceBetweenEyes;
	private double faceWidthOfEye;
	private double faceWidthOfMouth;
	private double faceLength;
	private double noseHeight;
	private double chinHeight;
	private double foreheadHeight;
	
	private double faceGoldenRatio;
	private double faceHeightRatio;
	private double eyesWidthRatio;
	private double faceWidthRatio;
	private double chinAngleTan;
	
	private double analyticResult;
	
	public Analyse() {
		for(int i = 0;i < 7;i++){
			measure[i] = 0;
		}
		distanceBetweenEyes = 0;
		faceWidthOfEye = 0;
		faceWidthOfMouth = 0;
		faceLength = 0;
		noseHeight = 0;
		chinHeight = 0;
		foreheadHeight = 0;
	}
	
	public void SetData(double data){
		for(int i = 0; i < 7; i++){
			if(isArrayEmpty(measure[i])){
				measure[i]= data;
				break;
			}
		}
	}

	private boolean isArrayEmpty(double data) {
		if(data == 0)
			return true;
		return false;
	}

	private int[] checkNotDoneStep(){
		int[] notDoneRecords=new int[7];
		for(int i = 0; i < 7; i++){
			if(measure[i]==0){
				notDoneRecords[i] = 1;
			}
		}
		return notDoneRecords;
	}
	public void calculate(){
		getAllInfomationFromArray();
		faceGoldenRatio = calculateFaceGoldenRatio();
		faceHeightRatio = calculateFaceHeightRatio();
		eyesWidthRatio = calculateEyesWidthRatio();
		faceWidthRatio = calculateFaceWidthRatio();
		chinAngleTan = calculateChinAngleTan();
		calculateResult();
		
	}

	private void getAllInfomationFromArray(){
		distanceBetweenEyes = measure[0];
		faceWidthOfEye = measure[1];
		faceWidthOfMouth = measure[2];
		faceLength = measure[3];
		noseHeight = measure[4];
		chinHeight = measure[5];
		foreheadHeight = measure[6];
	}	
	
	private double calculateFaceGoldenRatio() {
		return faceWidthOfEye/(faceLength+foreheadHeight);
		
	}
	
	private double calculateFaceHeightRatio() {
		return noseHeight/(faceLength+foreheadHeight);
		
	}

	private double calculateEyesWidthRatio() {
		return distanceBetweenEyes/faceWidthOfEye;
		
	}

	private double calculateFaceWidthRatio() {
		return faceWidthOfMouth/faceWidthOfEye;
		
	}

	private double calculateChinAngleTan() {
		return (faceWidthOfMouth/chinHeight)*0.5;
		
	}

	private void calculateResult() {
		analyticResult = Math.sqrt(calculateVariance(faceGoldenRatio, FACE_GOLDEN_RATIO)+calculateVariance(faceHeightRatio, FACE_HEIGHT_RATIO)
			+calculateVariance(eyesWidthRatio, EYES_WIGTH_RATIO)+calculateVariance(faceWidthRatio, FACE_WIDTH_RATIO)
			+calculateVariance(chinAngleTan, CHIN_ANGLE_TAN));		
	}

	private double calculateVariance(double actualValue,
			double exceptValue) {		
		return (actualValue - exceptValue)*(actualValue - exceptValue);
	}
	
	public double getAnalyticResult(){
		return analyticResult;
	}

	public int getStarLevel(){
		if(analyticResult<0.2)
			return 1;
		else if(analyticResult < 0.4)
			return 2;
		else if(analyticResult < 0.6)
			return 3;
		else if(analyticResult < 0.8)
			return 4;
		else
			return 0;
	}
}
