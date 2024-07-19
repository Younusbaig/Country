provider "aws" {
  region = "eu-west-2"
}

resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
  tags = {
    Name = "main-vpc"
  }
}

resource "aws_subnet" "main" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "eu-west-2a"
  tags = {
    Name = "main-subnet"
  }
}

resource "aws_security_group" "allow_http" {
  vpc_id = aws_vpc.main.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_iam_role" "ec2_role" {
  name = "ec2_role_countries"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_instance_profile" "ec2_profile" {
  name = "ec2_profile_countries"
  role = aws_iam_role.ec2_role.name
}

resource "aws_iam_role_policy_attachment" "ec2_role_attachment" {
  role       = aws_iam_role.ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess"
}

resource "aws_instance" "app_server" {
  ami           = "ami-09e1be1488e06c193"
  instance_type = "t4g.micro"
  subnet_id     = aws_subnet.main.id

  iam_instance_profile = aws_iam_instance_profile.ec2_profile.name



  tags = {
    Name = "AppServer"
  }

  user_data = <<-EOF
              #!/bin/bash
              sudo yum update -y
              sudo yum install -y java-1.8.0-openjdk aws-cli
              aws s3 cp s3://your-s3-bucket-name/my-app-0.0.1-SNAPSHOT.jar /home/ec2-user/
              java -jar /home/ec2-user/my-app-0.0.1-SNAPSHOT.jar
              EOF
}

output "instance_public_ip" {
  value = aws_instance.app_server.public_ip
}
