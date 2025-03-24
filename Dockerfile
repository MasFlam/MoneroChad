FROM python:3.13.2-alpine3.21

WORKDIR /code

RUN apk add --no-cache build-base

COPY ./requirements.txt /code/requirements.txt

RUN pip install --no-cache-dir --upgrade -r /code/requirements.txt

COPY ./monerochad /code/monerochad

CMD ["python3", "-m", "monerochad.main"]
