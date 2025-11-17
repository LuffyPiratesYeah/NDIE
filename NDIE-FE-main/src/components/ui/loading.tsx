export default function Loading() {
  return (
    <div className="fixed left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2 z-50">
      <div
        className="animate-spin rounded-full w-[10vw] h-[10vw] border-t-2 border-b-2 border-gray-900"
      ></div>
    </div>
  );
}