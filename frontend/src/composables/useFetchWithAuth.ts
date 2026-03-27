import { createFetch } from "@vueuse/core";
import { useAuth } from "./useAuth";

const { token } = useAuth();

const useFetchWithAuth = createFetch({
  combination: "overwrite",
  options: {
    async beforeFetch({ options }) {
      const headers = options.headers as Record<string, string> || {};
      headers["Authorization"] = `Bearer ${token}`;
      options.headers = headers;
      return { options };
    },
  },
});

export default useFetchWithAuth;
