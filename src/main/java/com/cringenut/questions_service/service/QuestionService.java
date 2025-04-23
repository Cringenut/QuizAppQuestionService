package com.cringenut.questions_service.service;

import com.cringenut.questions_service.Dao.QuestionDao;
import com.cringenut.questions_service.model.Question;
import com.cringenut.questions_service.model.QuestionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionDao.save(question);
            return new ResponseEntity<>("Question added", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, Integer numQuestions) {
        List<Integer> questions = questionDao.findRandomByCategory(category, numQuestions);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromIds(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Optional<Question>> questions = new ArrayList<>();

        for (Integer id : questionIds) {
            questions.add(questionDao.findById(id));
        }

        questions.stream().forEach((question -> {
            QuestionWrapper wrapper = new QuestionWrapper(
                    question.get().getQuestionTitle(),
                    question.get().getQuestion(),
                    question.get().getOption1(),
                    question.get().getOption2(),
                    question.get().getOption3(),
                    question.get().getOption4()
            );

            wrappers.add(wrapper);
        }));

        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }
}
