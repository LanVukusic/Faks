import { useNavigate } from "react-router-dom";

interface IProtectedprops {
  children: React.ReactNode;
}

export const ProtectedPath = ({ children }: IProtectedprops) => {
  const navigate = useNavigate();
  // const user = useFirebaseUser();
  // if (!user) {
  //   navigate("/login");
  //   return <></>;
  // }
  return children as JSX.Element;
};
