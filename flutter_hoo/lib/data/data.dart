class A{
  String getMessage() => 'A';
}

class B{
  String getMessage() => 'B';
}

class P{
  String getMessage() => 'P';
}

class AB extends P with A,B{

}

class BA extends P with B,A{

}

