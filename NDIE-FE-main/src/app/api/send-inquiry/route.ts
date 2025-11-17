// app/api/send-inquiry/route.ts
// 파일 상단에 import { NextResponse } from 'next/server'; 가 있는지 확인

import { NextResponse } from 'next/server'; // 필수
import nodemailer from 'nodemailer';

// POST 요청을 처리하는 함수를 'POST'라는 이름으로 export 합니다.
export async function POST(req: Request) { // 'export default' 제거, 함수 이름을 'POST'로 지정
  console.log('API Route: POST 요청 수신');

  try {
    const body = await req.json();
    const { name, email, organization, selectedTag, content } = body;

    console.log('API Route: 요청 바디 파싱 완료', body);

    // 환경 변수 검증 및 로그 (생략 가능하지만 디버깅에 유용)
    console.log('EMAIL_HOST:', process.env.EMAIL_HOST ? '설정됨' : '설정 안됨');
    console.log('EMAIL_PORT:', process.env.EMAIL_PORT ? '설정됨' : '설정 안됨');
    console.log('EMAIL_USER:', process.env.EMAIL_USER ? '설정됨' : '설정 안됨');
    console.log('EMAIL_PASS:', process.env.EMAIL_PASS ? '설정됨' : '설정 안됨 (보안상 값은 출력X)'); // 실제 값 출력 금지

    if (!process.env.EMAIL_HOST || !process.env.EMAIL_PORT || !process.env.EMAIL_USER || !process.env.EMAIL_PASS) {
      console.error("API Route: 이메일 환경 변수가 제대로 설정되지 않았습니다.");
      return NextResponse.json({ message: 'Server configuration error: Missing environment variables.' }, { status: 500 });
    }

    const transporter = nodemailer.createTransport({
      host: process.env.EMAIL_HOST,
      port: parseInt(process.env.EMAIL_PORT as string, 10),
      secure: process.env.EMAIL_PORT === '465', // true for 465, false for other ports (587 usually needs STARTTLS)
      auth: {
        user: process.env.EMAIL_USER,
        pass: process.env.EMAIL_PASS,
      },
      tls: {
        rejectUnauthorized: false // 개발 환경에서만 사용, 실제 서비스에서는 true 권장
      }
    });

    console.log('API Route: Nodemailer transporter 생성 완료');

    // const mailOptions = {
    //   from: `"${name}" <${email}>`,
    //   to: process.env.EMAIL_USER, // 이 문의를 받을 이메일 주소 (관리자 이메일 등)
    //   subject: `[문의] ${selectedTag} - ${name}님의 문의`,
    //   html: `
    //     <p><strong>이름:</strong> ${name}</p>
    //     <p><strong>단체/기관명:</strong> ${organization || '없음'}</p>
    //     <p><strong>이메일:</strong> ${email}</p>
    //     <p><strong>문의 태그:</strong> ${selectedTag}</p>
    //     <p><strong>문의 내용:</strong></p>
    //     <p>${content}</p>
    //   `,
    // };
    const mailOptions = {
      from: `"${name}" <${email}>`,
      to: "ploytechcourse@gmail.com", // 이 문의를 받을 이메일 주소 (관리자 이메일 등)
      subject: `[문의] ${selectedTag} - ${name}님의 문의`,
      html: `
        <p><strong>이름:</strong> ${name}</p>
        <p><strong>단체/기관명:</strong> ${organization || '없음'}</p>
        <p><strong>이메일:</strong> ${email}</p>
        <p><strong>문의 태그:</strong> ${selectedTag}</p>
        <p><strong>문의 내용:</strong></p>
        <p>${content}</p>
      `,
    };

    await transporter.sendMail(mailOptions);
    console.log('API Route: 이메일 발송 성공');

    return NextResponse.json({ message: '문의 이메일이 성공적으로 발송되었습니다.' }, { status: 200 });
  } catch (error) {
    console.error('API Route: 이메일 발송 또는 처리 실패:', error);
    const errorMessage = error instanceof Error ? error.message : '알 수 없는 오류가 발생했습니다.';
    return NextResponse.json({ message: `이메일 발송에 실패했습니다: ${errorMessage}` }, { status: 500 });
  }
}

// 만약 GET 요청도 허용하고 싶다면 다음과 같이 정의합니다.
// export async function GET(req: Request) {
//   return NextResponse.json({ message: 'GET request to send-inquiry API' }, { status: 200 });
// }