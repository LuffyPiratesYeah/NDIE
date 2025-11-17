import { Suspense } from 'react';
import LoginSuccessContent from '@/components/ui/LoginSuccessContent';

export default function LoginSuccessPage() {
  return (
    <Suspense fallback={<div>로그인 페이지 로딩 중...</div>}>
      <LoginSuccessContent />
    </Suspense>
  );
}