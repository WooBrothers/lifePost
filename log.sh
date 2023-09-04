# 로그 파일 경로
log_file="./myapp.log"

# 로그 메시지
log_message="This is a log message."

# 로그를 파일에 추가 (권한 문제를 방지하기 위해 sudo 사용)
sudo bash -c "echo $(date) - $log_message >> $log_file"

# 스크립트 실행 로그 출력
echo "Logged: $log_message"