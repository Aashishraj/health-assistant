package com.example.health;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.health.ml.Mlmodel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;

//import application.example.authenticator.ml.MachineModel;

public class Fever extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public Spinner spinnerTextSize,spinnerTextSize2,spinnerTextSize3,spinnerTextSize4,spinnerTextSize5;
//    public TextView txtHelloWorld,txtHelloWorld2,txtHelloWorld3,txtHelloWorld4,txtHelloWorld5,
    public TextView tt;
    int spinnersize=10;
    String[] valueFromSpinner=new String[spinnersize];


    Interpreter interpreter;

    // public String s[]=new String[5];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fever);


        Button predict_disease=findViewById(R.id.predict_disease);



        tt=findViewById(R.id.textView4);


//        txtHelloWorld = findViewById(R.id.txt_hello_world);
//        txtHelloWorld2 = findViewById(R.id.txt_hello_world2);
//        txtHelloWorld3 = findViewById(R.id.txt_hello_world3);
//        txtHelloWorld4 = findViewById(R.id.txt_hello_world4);
//        txtHelloWorld5 = findViewById(R.id.txt_hello_world5);

        spinnerTextSize = findViewById(R.id.symp1);
        spinnerTextSize2=findViewById(R.id.symp2);
        spinnerTextSize3=findViewById(R.id.symp3);
        spinnerTextSize4=findViewById(R.id.symp4);
        spinnerTextSize5=findViewById(R.id.symp5);


        spinnerTextSize.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        spinnerTextSize2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        spinnerTextSize3.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        spinnerTextSize4.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        spinnerTextSize5.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        /* String[] textSizes = getResources().getStringArray(R.array.symptoms1); */
        String textSizes[] ={"itching",
                "skin_rash",
                "nodal_skin_eruptions",
                "continuous_sneezing",
                "shivering",
                "chills",
                "joint_pain",
                "stomach_pain",
                "acidity",
                "ulcers_on_tongue",
                "muscle_wasting",
                "vomiting",
                "burning_micturition",
                "spotting_ urination",
                "fatigue",
                "weight_gain",
                "anxiety",
                "cold_hands_and_feets",
                "mood_swings",
                "weight_loss",
                "restlessness",
                "lethargy",
                "patches_in_throat",
                "irregular_sugar_level",
                "cough",
                "high_fever",
                "sunken_eyes",
                "breathlessness",
                "sweating",
                "dehydration",
                "indigestion",
                "headache",
                "yellowish_skin",
                "dark_urine",
                "nausea",
                "loss_of_appetite",
                "pain_behind_the_eyes",
                "back_pain",
                "constipation",
                "abdominal_pain",
                "diarrhoea",
                "mild_fever",
                "yellow_urine",
                "yellowing_of_eyes",
                "acute_liver_failure",
                "fluid_overload",
                "swelling_of_stomach",
                "swelled_lymph_nodes",
                "malaise",
                "blurred_and_distorted_vision",
                "phlegm",
                "throat_irritation",
                "redness_of_eyes",
                "sinus_pressure",
                "runny_nose",
                "congestion",
                "chest_pain",
                "weakness_in_limbs",
                "fast_heart_rate",
                "pain_during_bowel_movements",
                "pain_in_anal_region",
                "bloody_stool",
                "irritation_in_anus",
                "neck_pain",
                "dizziness",
                "cramps",
                "bruising",
                "obesity",
                "swollen_legs",
                "swollen_blood_vessels",
                "puffy_face_and_eyes",
                "enlarged_thyroid",
                "brittle_nails",
                "swollen_extremeties",
                "excessive_hunger",
                "extra_marital_contacts",
                "drying_and_tingling_lips",
                "slurred_speech",
                "knee_pain",
                "hip_joint_pain",
                "muscle_weakness",
                "stiff_neck",
                "swelling_joints",
                "movement_stiffness",
                "spinning_movements",
                "loss_of_balance",
                "unsteadiness",
                "weakness_of_one_body_side",
                "loss_of_smell",
                "bladder_discomfort",
                "foul_smell_of urine",
                "continuous_feel_of_urine",
                "passage_of_gases",
                "internal_itching",
                "toxic_look_(typhos)",
                "depression",
                "irritability",
                "muscle_pain",
                "altered_sensorium",
                "red_spots_over_body",
                "belly_pain",
                "abnormal_menstruation",
                "dischromic _patches",
                "watering_from_eyes",
                "increased_appetite",
                "polyuria",
                "family_history",
                "mucoid_sputum",
                "rusty_sputum",
                "lack_of_concentration",
                "visual_disturbances",
                "receiving_blood_transfusion",
                "receiving_unsterile_injections",
                "coma",
                "stomach_bleeding",
                "distention_of_abdomen",
                "history_of_alcohol_consumption",
                "fluid_overload.1",
                "blood_in_sputum",
                "prominent_veins_on_calf",
                "palpitations",
                "painful_walking",
                "pus_filled_pimples",
                "blackheads",
                "scurring",
                "skin_peeling",
                "silver_like_dusting",
                "small_dents_in_nails",
                "inflammatory_nails",
                "blister",
                "red_sore_around_nose",
                "yellow_crust_ooze"};

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, textSizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTextSize.setAdapter(adapter);

        ArrayAdapter adapter2 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, textSizes);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTextSize2.setAdapter(adapter2);

        ArrayAdapter adapter3 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, textSizes);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTextSize3.setAdapter(adapter3);

        ArrayAdapter adapter4 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, textSizes);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTextSize4.setAdapter(adapter4);

        ArrayAdapter adapter5 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, textSizes);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTextSize5.setAdapter(adapter5);

        HashMap<String,Integer> hmap=new HashMap<String,Integer>();

        for(int i=0;i<textSizes.length;i++)
        {
            hmap.put(textSizes[i],i);


        }

        predict_disease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = null;
                Toast.makeText(Fever.this, "You clicked predict", Toast.LENGTH_SHORT).show();
                try {
                    
                    Mlmodel model = Mlmodel.newInstance(getApplicationContext());

                    // Creates inputs for reference.



                    // Creates inputs for reference.
                    float[] input= new float[132];
                    for(int i=0;i<132;i++){
                        input[i]=0;
                    }
                    for(int i=0;i<5;i++){
                        input[hmap.get(valueFromSpinner[i])]=1;
                        Log.d("tag1"," "+ valueFromSpinner[i]);
                    }

//                    int srcLength = input.length;
//                    byte[]dst = new byte[srcLength << 2];
//
//                    for (int i=0; i<srcLength; i++) {
//                        int x = input[i];
//                        int j = i << 2;
//                        dst[j++] = (byte) ((x >>> 0) & 0xff);
//                        dst[j++] = (byte) ((x >>> 8) & 0xff);
//                        dst[j++] = (byte) ((x >>> 16) & 0xff);
//                        dst[j++] = (byte) ((x >>> 24) & 0xff);
//                    }

                    int capacity= 132*4;
                    ByteBuffer byteBuffer =  ByteBuffer.allocateDirect(1 * 132 * 4).order(ByteOrder.nativeOrder());
                    for(int i=0;i<132;i++){
                        byteBuffer.putFloat(input[i]);
                    }

                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 132}, DataType.FLOAT32);
                    inputFeature0.loadBuffer(byteBuffer);
                    Log.d("tag1"," "+byteBuffer);

                    // Runs model inference and gets result.

                    Mlmodel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    Log.d("tag1"," "+ outputFeature0.getFloatArray()[0]);
                    Toast.makeText(Fever.this, " "+ outputFeature0.getFloatArray()[0], Toast.LENGTH_SHORT).show();
                    tt.setText(outputFeature0.getFloatArray()[0]+ " ");

                    String[] out={"(vertigo) Paroymsal  Positional Vertigo", "AIDS", "Acne",
                            "Alcoholic hepatitis", "Allergy", "Arthritis", "Bronchial Asthma","Cervical spondylosis",
                            "Chicken pox", "Chronic cholestasis","Common Cold", "Dengue", "Diabetes ","Dimorphic hemmorhoids(piles)",
                            "Drug Reaction","Fungal infection", "GERD", "Gastroenteritis", "Heart attack","Hepatitis B", "Hepatitis C", "Hepatitis D",
                            "Hepatitis E","Hypertension ", "Hyperthyroidism", "Hypoglycemia","Hypothyroidism", "Impetigo", "Jaundice", "Malaria", "Migraine",
                            "Osteoarthristis", "Paralysis (brain hemorrhage)","Peptic ulcer diseae", "Pneumonia", "Psoriasis", "Tuberculosis","Typhoid",
                            "Urinary tract infection", "Varicose veins","hepatitis A"};

                    // Releases model resources if no longer used.

                    /////////////////////////////////////////////////////////////////////////////////////
                    String[] maxoutput=new String[3];
                    float[] maxaccuracy=new float[3];
                    HashMap<Float, Integer> maxmapoutput = new HashMap<Float, Integer>();
                    HashMap<Integer, String> maxmap = new HashMap<Integer, String>();
                    for(int i=0;i<out.length;i++) {
                        maxmap.put(i,out[i]);
                    }

                    for(int i=0;i<outputFeature0.getFloatArray().length;i++){
                        maxmapoutput.put(outputFeature0.getFloatArray()[i],i);
                    }

                    float[] arr=outputFeature0.getFloatArray();
                    String all="";
                    for(int i=0;i<arr.length;i++){
                        all+= out[i]+" : = "+arr[i]+"\n";
                    }
                    Log.d("tag3", all);
                    Arrays.sort(arr);
                    int j=0;
                    for(int i=arr.length-1;i>=0;i--){
                        if(j==3)
                            break;
                        maxaccuracy[j]=arr[i];
                        i--;
                        j++;
                    }

                    String answer="";
                    for(int i=0;i<j;i++){
                        maxoutput[i]= maxmap.get(maxmapoutput.get(maxaccuracy[i]));
                        answer+= maxoutput[i] + " = " +  String.valueOf(maxaccuracy[i]) + "\n";
                    }
                    Log.d("tag2", answer);
                    tt.setText(answer);




                } catch (IOException e) {
                    // TODO Handle the exception
                    Toast.makeText(Fever.this, "You cannot click", Toast.LENGTH_SHORT).show();

                }
                
                //String ans=doInference(s);




            }
        });

    }



    public String doInference(String [] s) {
        String input[]=new String[5];
        input[0]=s[0];
        input[1]=s[1];
        input[2]=s[2];
        input[3]=s[3];
        input[4]=s[4];

        String output=new String();
        interpreter.run(input,output);
        return  output;


    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.symp1) {
            valueFromSpinner[0] = parent.getItemAtPosition(position).toString();
            //txtHelloWorld.setTextSize(Float.parseFloat(valueFromSpinner));
        }
        if (parent.getId() == R.id.symp2) {
            valueFromSpinner[1] = parent.getItemAtPosition(position).toString();
            //txtHelloWorld.setTextSize(Float.parseFloat(valueFromSpinner));
        }
        if (parent.getId() == R.id.symp3) {
            valueFromSpinner[2] = parent.getItemAtPosition(position).toString();
            //txtHelloWorld.setTextSize(Float.parseFloat(valueFromSpinner));
        }
        if (parent.getId() == R.id.symp4) {
            valueFromSpinner[3] = parent.getItemAtPosition(position).toString();
            //txtHelloWorld.setTextSize(Float.parseFloat(valueFromSpinner));
        }
        if (parent.getId() == R.id.symp5) {
            valueFromSpinner[4] = parent.getItemAtPosition(position).toString();
//            txtHelloWorld.setText(valueFromSpinner[4]);
        }


    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Seelct something", Toast.LENGTH_SHORT).show();
    }
}