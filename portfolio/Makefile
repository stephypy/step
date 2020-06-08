install:
	npm install --no-package-lock --no-audit
	echo "#!/bin/sh\n\tcd portfolio && make format && make lint" > ../.git/hooks/pre-push
	chmod +x ../.git/hooks/pre-push

format: install
	npx prettier --write src/main/webapp/**/*.{html,css,js}
	mvn com.coveo:fmt-maven-plugin:format -Dverbose=true

lint: install
	npx eslint src/main/webapp/**/*.js
