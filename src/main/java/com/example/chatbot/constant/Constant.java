package com.example.chatbot.constant;

public class Constant {
    public static class Question {
        public static final String STAGE = "Bạn đang trong giai đoạn nào?";
        public static final String STAGE_SEASON = "Bạn có vừa tham gia giải đấu nào không?";
        public static final String STAGE_OTHER = "Bao lâu nữa thi bạn tham gia thi đấu (tháng) ?";
        public static final String AGE = "Bạn bao nhiêu tuổi?";
        public static final String SEX = "Giới tính của bạn là gì? (0: nữ , 1: nam)";
        public static final String WEIGHT = "Cân năng của bạn là bao nhiêu (kg)?";
        public static final String HEIGHT = "Chiều cao của bạn là bao nhiêu (cm)?";
        public static final String EXERCISE_INTENSITY = "Bạn thường tập luyện với cường độ như thế nào?";
        public static final String HABIT = "Bạn có thói quen gì trong tập luyện không?";
        public static final String HABIT_DETAIL = "Thói quen đó là gì?";
        public static final String SPORT = "Ngoài tập luyện thể hình bạn còn tham gia hoạt đông thể thao nào khác không?";
        public static final String SPORT_DETAIL = "Hoạt động đó là gì?";
        public static final String TKS = "Cảm ơn bạn đã tham gia buổi tư vấn của chúng tôi ngày hôm nay!";
        public static final String OVER = "Bot không thể đưa ra lời khuyên cho bạn, thông tin quy khách cung cấp sẽ được cập nhật trong tương lai!";
        public static final String RESULT = "Khẩu phần dinh dưỡng của bạn sẽ là: ";
        public static final String NOT_ENOUGH_DATA = "Hệ thống không thể đưa ra lời khuyên vì chưa đủ dữ liệu";
        public static final String INFO_USER = "Hệ thống sẽ dựa vào dữ liệu bạn cung cấp để đưa ra lời khuyên: ";
        public static final String CONFIRM = "Bạn có muốn nhận lời khuyên không?";
    }

    public static class Command {
        public static final String WELCOME = "Chào mừng bạn đến buổi tư vấn hôm nay vui lòng trả lời những câu hỏi sau để chuyên gia có chế độ hợp lý cho bạn";
        public static final String START = "Để bắt đầu với buổi tư vấn hãy gõ sẵn sàng";
        public static final String READY = "sẵn sàng";
        public static final String END = "kết thúc";
    }

    public static class NameElement {
        public static final String STAGE = "STAGE";
        public static final String BMI = "BMI";
        public static final String EXI = "EXI";
        public static final String HABIT = "HABIT";
        public static final String SPORT = "SPORT";
    }

    public static class Exception {
        public static final String ONLY_NUMBER = "Bot chỉ hiểu ở dạng số ở câu hỏi này?";
        public static final String BMI = "BMI";
        public static final String EXI = "EXI";
        public static final String HABIT = "HABIT";
        public static final String SPORT = "SPORT";
    }

    public static class YesNo {
        public static final String YES = "yes";
        public static final String NO = "no";
    }
}
