locate SecureDroid/app/src/main/ securedroid-backend/src/main/java/  | grep -E "\.java|\.xml" | xargs wc -l | grep -E "{1,}" | sort -nr > securedroid.txt
