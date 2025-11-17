const DEFAULT_BROWSER_API_BASE = 'http://localhost:8080';
const DEFAULT_SERVER_API_BASE = 'http://localhost:8080';

const resolveBrowserBase = () =>
  process.env.NEXT_PUBLIC_BROWSER_API_BASE ??
  process.env.NEXT_PUBLIC_API_BASE ??
  DEFAULT_BROWSER_API_BASE;

const resolveServerBase = () =>
  process.env.NEXT_INTERNAL_API_BASE ??
  process.env.NEXT_PUBLIC_API_BASE ??
  DEFAULT_SERVER_API_BASE;

export const getApiBase = (): string => {
  if (typeof window === 'undefined') {
    return resolveServerBase();
  }
  return resolveBrowserBase();
};

export const API_BASE = getApiBase();

export const getKakaoBase = (): string =>
  `${getApiBase()}/oauth2/authorization/kakao`;

export const KAKAO_BASE =
  process.env.NEXT_PUBLIC_KAKAO_BASE ?? getKakaoBase();
