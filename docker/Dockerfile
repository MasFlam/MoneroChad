FROM python:3.12.2-alpine3.19

WORKDIR /code

COPY ../requirements.txt /code/requirements.txt

RUN pip install --no-cache-dir --upgrade -r /code/requirements.txt

COPY ../monerochad /code/monerochad

CMD ["python3", "-m", "monerochad.main"]
