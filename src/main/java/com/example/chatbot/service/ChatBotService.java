package com.example.chatbot.service;

import com.example.chatbot.constant.Constant;
import com.example.chatbot.dto.AnswerUser;
import com.example.chatbot.dto.BmiRequest;
import com.example.chatbot.dto.ResponseView;
import com.example.chatbot.model.*;
import com.example.chatbot.repository.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

@Service
public class ChatBotService {

    private final Chat chatSession;

    private final StageRepository stageRepository;

    private final BmiRepository bmiRepository;

    private final CaseBaseRepository caseBaseRepository;

    private final ExerciseIntensityRepository exerciseIntensityRepository;

    private final HabitRepository habitRepository;

    private final OtherSportRepository otherSportRepository;

    private final SimilarWeightsRepository similarWeightsRepository;

    private String questionPre = "";

    private AnswerUser answerUser = new AnswerUser();

    private Stack<String> questions = new Stack<>();

    private Integer flap = -1;

    private Integer flapStage = -1;

    public ChatBotService(Bot alice, StageRepository stageRepository, BmiRepository bmiRepository,
                          CaseBaseRepository caseBaseRepository, ExerciseIntensityRepository exerciseIntensityRepository,
                          HabitRepository habitRepository, OtherSportRepository otherSportRepository,
                          SimilarWeightsRepository similarWeightsRepository) {
        chatSession = new Chat(alice);
        this.stageRepository = stageRepository;
        this.bmiRepository = bmiRepository;
        this.caseBaseRepository = caseBaseRepository;
        this.exerciseIntensityRepository = exerciseIntensityRepository;
        this.habitRepository = habitRepository;
        this.otherSportRepository = otherSportRepository;
        this.similarWeightsRepository = similarWeightsRepository;
    }

    //    public String sendMessage(String text) {
//        String answer = chatSession.multisentenceRespond(text);
//        System.out.print(answer);
//        return answer;
//    }
    private void init() {
        questions = new Stack<>();
        questions.push(Constant.Question.SPORT);
        questions.push(Constant.Question.HABIT);
        questions.push(Constant.Question.EXERCISE_INTENSITY);
        questions.push(Constant.Question.HEIGHT);
        questions.push(Constant.Question.WEIGHT);
        questions.push(Constant.Question.SEX);
        questions.push(Constant.Question.AGE);
        questions.push(Constant.Question.STAGE);
        flap = 0;
        questionPre = "";
        answerUser = new AnswerUser();
    }

    public ResponseView sendMessage(String text) {
        ResponseView responseView = new ResponseView();
        String response = text.toLowerCase();
        if (response.equals(Constant.Command.END)) {
            flap = -1;
        }
        // Check to start
        if (flap == -1) {
            if (Constant.Command.READY.equals(response)) {
                init();
                questionPre = questions.pop();
                responseView.setReply(Constant.Command.WELCOME + "\n" + questionPre);
                return responseView;
            }
            responseView.setReply(Constant.Command.START);
            return responseView;
        }
        // Question Info
        else if (flap == 0) {
            switch (questionPre) {
                case Constant.Question.STAGE:
                    Stage stage = stageRepository.findByDescriptionContaining(response);
                    if (stage == null) {
                        if (flapStage == -1) {
                            questionPre = Constant.Question.STAGE;
                            flapStage = 0;
                            responseView.setReply(Constant.Question.STAGE_SEASON);
                            responseView.setIsYesNo(true);
                            return responseView;
                        } else if (flapStage == 0 && Constant.YesNo.YES.equals(response)) {
                            response = "phục hồi";
                        } else if (flapStage == 0 && Constant.YesNo.NO.equals(response)) {
                            questionPre = Constant.Question.STAGE;
                            flapStage = 1;
                            responseView.setReply(Constant.Question.STAGE_OTHER);
                            return responseView;
                        } else if (flapStage == 1) {
                            try {
                                int month = Integer.parseInt(response);
                                if (month > 3) {
                                    response = "tăng cơ";
                                } else if (month <= 3 && month > 0) {
                                    response = "ăn kiêng";
                                } else {
                                    response = "thi đấu";
                                }
                            } catch (Exception e) {
                                questionPre = Constant.Question.STAGE;
                                responseView.setReply(Constant.Exception.ONLY_NUMBER);
                                return responseView;
                            }
                        }
                    }
                    flapStage = -1;
                    answerUser.setStage(response);
                    break;
                case Constant.Question.AGE:
                    try {
                        answerUser.setAge(Integer.parseInt(response));
                    } catch (Exception e) {
                        questionPre = Constant.Question.AGE;
                        responseView.setReply(Constant.Exception.ONLY_NUMBER);
                        return responseView;
                    }
                    break;
                case Constant.Question.SEX:
                    try {
                        answerUser.setSex(Integer.parseInt(response));
                    } catch (Exception e) {
                        questionPre = Constant.Question.SEX;
                        responseView.setReply(Constant.Exception.ONLY_NUMBER);
                        return responseView;
                    }
                    break;
                case Constant.Question.WEIGHT:
                    try {
                        answerUser.setWeight(Float.parseFloat(response));
                    } catch (Exception e) {
                        questionPre = Constant.Question.WEIGHT;
                        responseView.setReply(Constant.Exception.ONLY_NUMBER);
                        return responseView;
                    }
                    break;
                case Constant.Question.HEIGHT:
                    try {
                        answerUser.setHeight(Integer.parseInt(response));
                    } catch (Exception e) {
                        questionPre = Constant.Question.HEIGHT;
                        responseView.setReply(Constant.Exception.ONLY_NUMBER);
                        return responseView;
                    }
                    break;
                case Constant.Question.EXERCISE_INTENSITY:
                    answerUser.setExerciseIntensity(response);
                    break;
                case Constant.Question.HABIT:
                    if (Constant.YesNo.YES.equals(response)) {
                        questionPre = Constant.Question.HABIT;
                        responseView.setReply(Constant.Question.HABIT_DETAIL);
                        return responseView;
                    } else if (Constant.YesNo.NO.equals(response)) {
                        response = "không";
                    }
                    answerUser.setHabit(response);
                    break;
                case Constant.Question.SPORT:
                    if (Constant.YesNo.YES.equals(response)) {
                        questionPre = Constant.Question.SPORT;
                        responseView.setReply(Constant.Question.SPORT_DETAIL);
                        return responseView;
                    } else if (Constant.YesNo.NO.equals(response)) {
                        response = "không";
                    }
                    questionPre = "";
                    answerUser.setSport(response);
                    break;
                default:
                    break;
            }

            // Confirm and Result
            if (questions.size() == 0) {
                if (Constant.YesNo.YES.equals(response)) {
                    responseView.setReply(handleInput());
                    flap = -1;
                    return responseView;
                } else if (Constant.YesNo.NO.equals(response)) {
                    flap = -1;
                    responseView.setReply(Constant.Question.TKS);
                    return responseView;
                }
                responseView.setReply(Constant.Question.INFO_USER + "\n" + changeInputToTable() + "\n" + Constant.Question.CONFIRM);
                responseView.setIsYesNo(true);
                return responseView;
            }
            questionPre = questions.peek();
            if (questionPre.equals(Constant.Question.HABIT) || questionPre.equals(Constant.Question.SPORT)) {
                responseView.setIsYesNo(true);
            }
            responseView.setReply(questions.pop());
            return responseView;
        }
        return responseView;
    }

    private String changeInputToTable() {
        return answerUser.toString();
    }

    private String handleInput() {
        float maxCb = 0;
        int countError = 0;
        CaseBase caseBase = new CaseBase();
        Stage stage = stageRepository.findByDescriptionContaining(answerUser.getStage());
        if (stage == null) {
            countError += 1;
            stage = stageRepository.findById(1L).get();
        }
        Bmi bmi = bmiRepository.findByValue(getBmi(new BmiRequest(answerUser.getSex(), answerUser.getAge(), answerUser.getWeight(), answerUser.getHeight())));
        ExerciseIntensity exerciseIntensity = exerciseIntensityRepository.findByDescriptionContaining(answerUser.getExerciseIntensity());
        if (exerciseIntensity == null) {
            countError += 1;
            exerciseIntensity = exerciseIntensityRepository.findById(1L).get();
        }
        Habit habit = habitRepository.findByDescriptionContaining(answerUser.getHabit());
        if (habit == null) {
            countError += 1;
            habit = habitRepository.findById(4L).get();
        }
        OtherSport otherSport = otherSportRepository.findByDescriptionContaining(answerUser.getSport());
        if (otherSport == null) {
            countError += 1;
            otherSport = otherSportRepository.findById(1L).get();
        }
        if (countError >= 3) {
            return Constant.Question.NOT_ENOUGH_DATA;
        }
        List<CaseBase> caseBaseList = (List<CaseBase>) caseBaseRepository.findAll();
        for (CaseBase cb : caseBaseList) {
            float valueStage = (6 * similarWeightsRepository.findByNameAndCaseFromIdAndCaseToId(Constant.NameElement.STAGE,
                    stage.getId(), cb.getStageId()).getValue());
            float valueBmi = (4 * similarWeightsRepository.findByNameAndCaseFromIdAndCaseToId(Constant.NameElement.BMI,
                    bmi.getId(), cb.getBmiId()).getValue());
            float valueExi = (2 * similarWeightsRepository.findByNameAndCaseFromIdAndCaseToId(Constant.NameElement.EXI,
                    exerciseIntensity.getId(), cb.getExerciseId()).getValue());
            float valueHabit = (1 * similarWeightsRepository.findByNameAndCaseFromIdAndCaseToId(Constant.NameElement.HABIT,
                    habit.getId(), cb.getHabitId()).getValue());
            float valueSport = (1 * similarWeightsRepository.findByNameAndCaseFromIdAndCaseToId(Constant.NameElement.SPORT,
                    otherSport.getId(), cb.getOtherSportId()).getValue());
            float sum = (valueStage + valueBmi + valueExi + valueHabit + valueSport) / 14;
            if (sum > maxCb) {
                maxCb = sum;
                caseBase = cb;
            }
        }
        if (maxCb < 0.7) {
            return Constant.Question.OVER;
        }
        return Constant.Question.RESULT + '\n' + caseBase.getNutrition() + '\n' + Constant.Question.TKS;
    }


    public Float getBmi(BmiRequest bmi) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:5000/bmi-perdic";
        Map<String, Object> body = new HashMap<>();
        body.put("bmi", bmi);
        String jsonRequest = null;
        try {
            jsonRequest = mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert jsonRequest != null;
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, httpHeaders);
        Map<String, String> result = restTemplate.postForObject(url, request, HashMap.class);
        return Float.valueOf(result.get("result"));
    }


    private final ObjectMapper mapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

}
